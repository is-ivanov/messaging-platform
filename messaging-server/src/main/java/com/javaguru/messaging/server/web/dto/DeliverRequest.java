package com.javaguru.messaging.server.web.dto;

import com.javaguru.messaging.server.domain.ChatType;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

/** Тело {@code POST /internal/deliver} от Connection Management — кого и что доставить локально. */
public record DeliverRequest(
        @NotBlank String recipientUserId,
        @NotBlank String chatId,
        ChatType chatType,
        String senderId,
        String text,
        Instant timestamp,
        long seq
) {
}
