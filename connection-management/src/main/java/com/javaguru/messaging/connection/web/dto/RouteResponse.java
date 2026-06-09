package com.javaguru.messaging.connection.web.dto;

import com.javaguru.messaging.connection.domain.ChatType;

import java.time.Instant;
import java.util.List;

/**
 * Результат маршрутизации (по сути ACK отправителю + диагностика доставки).
 *
 * @param recipientCount  всего получателей (без отправителя)
 * @param deliveredTo     кому реально доставили онлайн
 * @param offline         получатели не в сети (нет соединения в реестре) — заберут историей
 * @param failedDelivery  онлайн, но доставка по REST упала (сервер недоступен и т.п.)
 */
public record RouteResponse(
        String chatId,
        ChatType chatType,
        String messageId,
        long seq,
        Instant timestamp,
        int recipientCount,
        List<String> deliveredTo,
        List<String> offline,
        List<String> failedDelivery
) {
}
