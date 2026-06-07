package com.javaguru.messaging.groups.error;

public record ErrorResponse(
        String code,
        String message
) {
}
