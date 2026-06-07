package com.javaguru.messaging.users.web.dto;

import com.javaguru.messaging.users.domain.UserStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Вызывается Messaging Service при connect/disconnect по WebSocket.
 */
public record StatusUpdateRequest(
        @NotNull UserStatus status
) {
}
