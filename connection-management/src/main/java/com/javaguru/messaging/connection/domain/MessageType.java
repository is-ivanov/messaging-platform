package com.javaguru.messaging.connection.domain;

/**
 * Тип входящего запроса на отправку (из WS-конверта {@code {type, payload}}).
 * - SEND_DIRECT  — личное 1-на-1 (chatId резолвится лениво из участников);
 * - SEND_GROUP   — в группу (chatId известен);
 * - SEND_CHANNEL — в канал (chatId известен).
 */
public enum MessageType {
    SEND_DIRECT,
    SEND_GROUP,
    SEND_CHANNEL
}
