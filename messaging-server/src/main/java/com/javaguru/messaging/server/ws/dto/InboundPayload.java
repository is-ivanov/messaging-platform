package com.javaguru.messaging.server.ws.dto;

/**
 * Полезная нагрузка входящего сообщения. {@code senderId} здесь НЕТ намеренно —
 * личность отправителя берётся из auth-контекста соединения (анти-спуфинг).
 */
public record InboundPayload(
        String toUserId,
        String chatId,
        String text
) {
}
