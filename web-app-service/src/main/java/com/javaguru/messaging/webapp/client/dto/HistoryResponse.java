package com.javaguru.messaging.webapp.client.dto;

import java.util.List;

/** Ответ Chat History {@code GET /api/chats/{chatId}/messages}. */
public record HistoryResponse(
        String chatId,
        List<MessageView> messages,
        Long nextBeforeSeq,
        boolean hasMore
) {
}
