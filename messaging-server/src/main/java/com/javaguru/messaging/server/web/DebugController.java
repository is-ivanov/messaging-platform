package com.javaguru.messaging.server.web;

import com.javaguru.messaging.server.client.ConnectionManagementClient;
import com.javaguru.messaging.server.client.dto.RouteRequest;
import com.javaguru.messaging.server.client.dto.RouteResponse;
import com.javaguru.messaging.server.domain.MessageType;
import com.javaguru.messaging.server.web.dto.DirectMessageRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Отладочная отправка 1-на-1 по HTTP (без WS-клиента): то же, что SEND_DIRECT.
 * Личность отправителя — из заголовка {@code X-User-Id} (имитация auth-контекста за шлюзом),
 * не из тела. Удобно, чтобы триггерить доставку WS-получателю из обычного HTTP-клиента.
 */
@RestController
@RequestMapping("/api/messages")
public class DebugController {

    private final ConnectionManagementClient cm;

    public DebugController(ConnectionManagementClient cm) {
        this.cm = cm;
    }

    @PostMapping("/direct")
    public RouteResponse direct(@RequestHeader("X-User-Id") String senderId,
                                @Valid @RequestBody DirectMessageRequest req) {
        return cm.route(new RouteRequest(senderId, MessageType.SEND_DIRECT, null, req.toUserId(), req.text()));
    }
}
