package com.javaguru.messaging.connection.error;

public record ErrorResponse(
        String code,
        String message
) {
}
