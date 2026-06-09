package com.javaguru.messaging.server.client.dto;

import com.javaguru.messaging.server.domain.MessageType;

/**
 * Тело запроса в Connection Management: {@code POST /internal/messages/route}.
 * {@code senderId} проставляет сам Messaging Server из auth-контекста соединения (не из payload клиента).
 */
public record RouteRequest(
        String senderId,
        MessageType type,
        String chatId,
        String toUserId,
        String text
) {
}
