package com.javaguru.messaging.chathistory.web.dto;

import com.javaguru.messaging.chathistory.domain.ChatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Запрос на сохранение сообщения (internal, вызывает Messaging Service).
 * senderId — личность автора из auth-контекста доверенного оркестратора, не из клиентского payload.
 */
public record StoreMessageRequest(
        @NotNull ChatType chatType,
        @NotBlank String chatId,
        @NotBlank String senderId,
        @NotBlank @Size(max = 10_000) String text
) {
}
