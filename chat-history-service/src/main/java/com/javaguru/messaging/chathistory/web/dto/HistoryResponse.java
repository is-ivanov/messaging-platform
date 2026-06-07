package com.javaguru.messaging.chathistory.web.dto;

import java.util.List;

/**
 * Страница истории чата (курсорная пагинация).
 * messages — новейшие сверху (seq по убыванию).
 * nextBeforeSeq — курсор для следующей (более старой) страницы: передать как ?beforeSeq=.
 *                 null, если сообщений нет.
 * hasMore — эвристика «страница заполнена целиком», т.е. возможно есть ещё более старые сообщения.
 */
public record HistoryResponse(
        String chatId,
        List<MessageView> messages,
        Long nextBeforeSeq,
        boolean hasMore
) {
}
