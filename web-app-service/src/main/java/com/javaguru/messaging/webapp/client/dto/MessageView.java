package com.javaguru.messaging.webapp.client.dto;

import java.time.Instant;

/** Элемент истории из Chat History (chatType приходит строкой "GROUP"/"CHANNEL"). */
public record MessageView(
        String messageId,
        String chatType,
        String chatId,
        String senderId,
        String text,
        Instant timestamp,
        long seq
) {
}
