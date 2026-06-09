package com.javaguru.messaging.connection.client.dto;

import com.javaguru.messaging.connection.domain.ChatType;

import java.time.Instant;

/**
 * Тело запроса доставки на Messaging Server: {@code POST {serverUrl}/internal/deliver}.
 * Сервер обязан запушить это в локальный WebSocket пользователя {@code recipientUserId}
 * как конверт {@code { "type": "MESSAGE", "payload": { ... } }}.
 */
public record DeliverRequest(
        String recipientUserId,
        String chatId,
        ChatType chatType,
        String senderId,
        String text,
        Instant timestamp,
        long seq
) {
}
