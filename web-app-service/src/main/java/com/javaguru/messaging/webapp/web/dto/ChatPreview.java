package com.javaguru.messaging.webapp.web.dto;

import com.javaguru.messaging.webapp.client.dto.MessageView;

import java.util.List;

/** Чат + последние сообщения для homepage. {@code name} есть только у каналов. */
public record ChatPreview(
        String chatId,
        String chatType,
        String name,
        List<MessageView> lastMessages
) {
}
