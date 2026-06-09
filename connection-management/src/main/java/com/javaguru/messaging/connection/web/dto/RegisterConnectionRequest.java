package com.javaguru.messaging.connection.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Регистрация соединения: тело {@code PUT /internal/connections/{userId}}.
 * {@code serverUrl} — базовый адрес того Messaging Server, к которому подключился пользователь
 * (например {@code http://localhost:8084}).
 */
public record RegisterConnectionRequest(
        @NotBlank String serverUrl
) {
}
