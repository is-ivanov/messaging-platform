package com.javaguru.messaging.server.client.dto;

/** Тело запроса в Users: {@code POST /internal/auth/validate}. */
public record ValidateRequest(
        String token
) {
}
