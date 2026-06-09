package com.javaguru.messaging.connection.client.dto;

import com.javaguru.messaging.connection.domain.ChatType;

/** Тело запроса в Chat History: {@code POST /internal/messages}. */
public record StoreMessageRequest(
        ChatType chatType,
        String chatId,
        String senderId,
        String text
) {
}
