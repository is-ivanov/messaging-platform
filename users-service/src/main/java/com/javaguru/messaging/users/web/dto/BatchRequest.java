package com.javaguru.messaging.users.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Получить профили по списку id (для сборки homepage в Messaging/Web App Service).
 */
public record BatchRequest(
        @NotNull List<String> ids
) {
}
