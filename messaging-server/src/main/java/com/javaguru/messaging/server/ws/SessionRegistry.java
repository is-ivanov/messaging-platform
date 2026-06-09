package com.javaguru.messaging.server.ws;

import tools.jackson.databind.ObjectMapper;
import com.javaguru.messaging.server.ws.dto.OutboundEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Локальные WebSocket-соединения ЭТОГО инстанса: {@code userId → session}.
 * Глобальную карту «кто на каком сервере» держит Connection Management — здесь только свои.
 */
@Component
public class SessionRegistry {

    private static final Logger log = LoggerFactory.getLogger(SessionRegistry.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;

    public SessionRegistry(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void add(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    /** Снять соединение, только если в карте всё ещё именно эта сессия (защита от гонки реконнекта). */
    public void remove(String userId, WebSocketSession session) {
        sessions.remove(userId, session);
    }

    public int size() {
        return sessions.size();
    }

    /** Отправить конверт пользователю, если он подключён к ЭТОМУ серверу. true — доставлено. */
    public boolean sendTo(String userId, OutboundEnvelope envelope) {
        WebSocketSession session = sessions.get(userId);
        if (session == null || !session.isOpen()) {
            return false;
        }
        return sendRaw(session, envelope);
    }

    /** Отправить конверт в конкретную сессию (например, ACK отправителю). */
    public boolean sendRaw(WebSocketSession session, OutboundEnvelope envelope) {
        try {
            String json = mapper.writeValueAsString(envelope);
            // sendMessage не потокобезопасен: к одной сессии могут писать WS-поток (ACK) и HTTP-поток (deliver).
            synchronized (session) {
                session.sendMessage(new TextMessage(json));
            }
            return true;
        } catch (Exception e) {
            log.warn("Не удалось отправить {} в сессию: {}", envelope.type(), e.toString());
            return false;
        }
    }
}
