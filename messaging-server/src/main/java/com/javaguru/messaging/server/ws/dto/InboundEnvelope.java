package com.javaguru.messaging.server.ws.dto;

import com.javaguru.messaging.server.domain.MessageType;

/** Входящий WS-конверт от клиента: {@code { "type": ..., "payload": { ... } }}. */
public record InboundEnvelope(
        MessageType type,
        InboundPayload payload
) {
}
