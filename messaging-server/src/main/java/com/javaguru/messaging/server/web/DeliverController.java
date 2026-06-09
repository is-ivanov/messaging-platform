package com.javaguru.messaging.server.web;

import com.javaguru.messaging.server.web.dto.DeliverRequest;
import com.javaguru.messaging.server.ws.SessionRegistry;
import com.javaguru.messaging.server.ws.dto.OutboundEnvelope;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Доставка локально подключённому получателю — этот эндпоинт зовёт Connection Management,
 * когда по реестру получатель висит на ЭТОМ инстансе. Пушит конверт {@code MESSAGE} в его WebSocket.
 *
 * <p>404, если получателя здесь уже нет (отключился) — CM зачтёт это как failedDelivery.
 */
@RestController
@RequestMapping("/internal")
public class DeliverController {

    private final SessionRegistry sessions;

    public DeliverController(SessionRegistry sessions) {
        this.sessions = sessions;
    }

    @PostMapping("/deliver")
    public ResponseEntity<Void> deliver(@Valid @RequestBody DeliverRequest req) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("chatId", req.chatId());
        payload.put("chatType", req.chatType());
        payload.put("senderId", req.senderId());
        payload.put("text", req.text());
        payload.put("createdAt", req.timestamp());
        payload.put("seq", req.seq());

        boolean delivered = sessions.sendTo(req.recipientUserId(), new OutboundEnvelope("MESSAGE", payload));
        return delivered ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
