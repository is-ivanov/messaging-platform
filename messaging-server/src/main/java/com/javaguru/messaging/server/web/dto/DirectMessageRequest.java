package com.javaguru.messaging.server.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Тело отладочного {@code POST /api/messages/direct} (отправка 1-на-1 без WS-клиента). */
public record DirectMessageRequest(
        @NotBlank String toUserId,
        @NotBlank @Size(max = 10_000) String text
) {
}
