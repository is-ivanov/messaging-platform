package com.javaguru.messaging.connection.client.dto;

import com.javaguru.messaging.connection.domain.ChatType;

import java.time.Instant;

/** Ответ Chat History на сохранение — присвоенные messageId, seq, timestamp. */
public record StoredMessage(
        String messageId,
        ChatType chatType,
        String chatId,
        String senderId,
        String text,
        Instant timestamp,
        long seq
) {
}
