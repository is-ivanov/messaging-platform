package com.javaguru.messaging.server.client.dto;

import com.javaguru.messaging.server.domain.ChatType;

import java.util.List;

/**
 * Ответ Connection Management на маршрутизацию. Берём поля, нужные для ACK и отладки;
 * остальные Jackson игнорирует (FAIL_ON_UNKNOWN_PROPERTIES=false в Spring Boot).
 */
public record RouteResponse(
        String chatId,
        ChatType chatType,
        long seq,
        List<String> deliveredTo,
        List<String> offline,
        List<String> failedDelivery
) {
}
