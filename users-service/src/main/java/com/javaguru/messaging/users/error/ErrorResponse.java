package com.javaguru.messaging.users.error;

public record ErrorResponse(
        String code,
        String message
) {
}
