package com.javaguru.messaging.connection.web.dto;

import com.javaguru.messaging.connection.domain.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Запрос на маршрутизацию сообщения от тонкого Messaging Server: {@code POST /internal/messages/route}.
 *
 * <p>{@code senderId} приходит в теле, но это НЕ спуфинг: его проставляет доверенный Messaging Server
 * из auth-контекста соединения (клиент его не присылает). CM доверяет оркестратору — та же конвенция,
 * что у chat-history {@code /internal/messages}.
 *
 * <p>{@code chatId} обязателен для SEND_GROUP/SEND_CHANNEL; {@code toUserId} — для SEND_DIRECT
 * (проверяется в RoutingService по типу).
 */
public record RouteRequest(
        @NotBlank String senderId,
        @NotNull MessageType type,
        String chatId,
        String toUserId,
        @NotBlank @Size(max = 10_000) String text
) {
}
