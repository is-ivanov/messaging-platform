package com.javaguru.messaging.chathistory.web;

import com.javaguru.messaging.chathistory.service.ChatHistoryService;
import com.javaguru.messaging.chathistory.web.dto.HistoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Внешний API: недавняя история чата с курсорной пагинацией (homepage / возврат онлайн, слайды 102–103).
 * Неизвестный chatId -> пустая страница (чат без сообщений — валидная ситуация), не 404.
 */
@RestController
@RequestMapping("/api/chats")
public class HistoryController {

    private final ChatHistoryService service;

    public HistoryController(ChatHistoryService service) {
        this.service = service;
    }

    @GetMapping("/{chatId}/messages")
    public HistoryResponse history(
            @PathVariable String chatId,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) Long beforeSeq
    ) {
        return service.history(chatId, limit, beforeSeq);
    }
}
