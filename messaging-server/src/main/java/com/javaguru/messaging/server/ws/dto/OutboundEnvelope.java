package com.javaguru.messaging.server.ws.dto;

/** Исходящий WS-конверт серверу→клиенту: {@code MESSAGE} / {@code ACK} / {@code ERROR}. */
public record OutboundEnvelope(
        String type,
        Object payload
) {
}
