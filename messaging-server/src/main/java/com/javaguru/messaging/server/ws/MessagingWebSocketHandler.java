package com.javaguru.messaging.server.ws;

import tools.jackson.databind.ObjectMapper;
import com.javaguru.messaging.server.client.ConnectionManagementClient;
import com.javaguru.messaging.server.client.dto.RouteRequest;
import com.javaguru.messaging.server.client.dto.RouteResponse;
import com.javaguru.messaging.server.config.AppProperties;
import com.javaguru.messaging.server.ws.dto.InboundEnvelope;
import com.javaguru.messaging.server.ws.dto.OutboundEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * Тонкий обработчик WebSocket. Логики доставки здесь НЕТ — только I/O:
 * <ul>
 *   <li>connect/disconnect → регистрация в Connection Management;</li>
 *   <li>входящее → пересылка в Connection Management (senderId из auth-контекста) → ACK/ERROR.</li>
 * </ul>
 */
@Component
public class MessagingWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MessagingWebSocketHandler.class);

    private final SessionRegistry sessions;
    private final ConnectionManagementClient cm;
    private final AppProperties app;
    private final ObjectMapper mapper;

    public MessagingWebSocketHandler(SessionRegistry sessions,
                                     ConnectionManagementClient cm,
                                     AppProperties app,
                                     ObjectMapper mapper) {
        this.sessions = sessions;
        this.cm = cm;
        this.app = app;
        this.mapper = mapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = userId(session);
        sessions.add(userId, session);
        cm.register(userId, app.serverUrl());
        log.info("WS connect userId={} → {} (соединений на инстансе: {})", userId, app.serverUrl(), sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String senderId = userId(session);

        InboundEnvelope envelope;
        try {
            envelope = mapper.readValue(message.getPayload(), InboundEnvelope.class);
        } catch (Exception e) {
            sessions.sendRaw(session, error("невалидный конверт: " + e.getMessage()));
            return;
        }
        if (envelope.type() == null || envelope.payload() == null) {
            sessions.sendRaw(session, error("ожидается { type, payload }"));
            return;
        }

        // senderId — из auth-контекста соединения, НЕ из payload (анти-спуфинг).
        RouteRequest request = new RouteRequest(
                senderId,
                envelope.type(),
                envelope.payload().chatId(),
                envelope.payload().toUserId(),
                envelope.payload().text());

        try {
            RouteResponse resp = cm.route(request);
            sessions.sendRaw(session, new OutboundEnvelope("ACK", Map.of(
                    "chatId", resp.chatId(),
                    "seq", resp.seq())));
        } catch (Exception e) {
            log.warn("Маршрутизация не удалась для senderId={}: {}", senderId, e.toString());
            sessions.sendRaw(session, error(e.getMessage() == null ? e.toString() : e.getMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = userId(session);
        if (userId != null) {
            sessions.remove(userId, session);
            cm.deregister(userId);
            log.info("WS disconnect userId={} status={}", userId, status);
        }
    }

    private static OutboundEnvelope error(String message) {
        return new OutboundEnvelope("ERROR", Map.of("message", message));
    }

    private static String userId(WebSocketSession session) {
        return (String) session.getAttributes().get(AuthHandshakeInterceptor.USER_ID_ATTR);
    }
}
