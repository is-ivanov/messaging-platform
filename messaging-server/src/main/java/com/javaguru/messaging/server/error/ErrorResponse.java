package com.javaguru.messaging.server.error;

public record ErrorResponse(
        String code,
        String message
) {
}
