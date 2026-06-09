package com.javaguru.messaging.server.client.dto;

/** Ответ Users: userId по валидному токену. */
public record ValidateResponse(
        String userId
) {
}
