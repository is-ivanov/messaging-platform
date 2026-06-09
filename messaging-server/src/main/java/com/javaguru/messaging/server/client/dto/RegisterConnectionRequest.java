package com.javaguru.messaging.server.client.dto;

/** Тело запроса в Connection Management: {@code PUT /internal/connections/{userId}}. */
public record RegisterConnectionRequest(
        String serverUrl
) {
}
