package com.javaguru.messaging.webapp.error;

public record ErrorResponse(
        String code,
        String message
) {
}
