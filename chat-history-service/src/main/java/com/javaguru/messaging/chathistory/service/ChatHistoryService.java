package com.javaguru.messaging.chathistory.service;

import com.javaguru.messaging.chathistory.domain.Message;
import com.javaguru.messaging.chathistory.repository.MessageRepository;
import com.javaguru.messaging.chathistory.web.dto.HistoryResponse;
import com.javaguru.messaging.chathistory.web.dto.MessageView;
import com.javaguru.messaging.chathistory.web.dto.StoreMessageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ChatHistoryService {

    static final int DEFAULT_LIMIT = 20;
    static final int MAX_LIMIT = 100;

    private final MessageRepository repository;

    public ChatHistoryService(MessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Сохранить сообщение, назначив следующий per-chat seq.
     *
     * Назначение seq = max(seq)+1 в одной транзакции; уникальный индекс (chat_id, seq) защищает
     * от дублей. Для учебного single-instance этого достаточно.
     * // TODO: NFR — при нескольких инстансах гонка max+1 ломается: нужен per-chat атомарный
     *    счётчик / БД-секвенция / выдача seq брокером (слайды 124–146).
     */
    @Transactional
    public Message store(StoreMessageRequest req) {
        long nextSeq = repository.findMaxSeq(req.chatId()) + 1;
        Message message = new Message(
                UUID.randomUUID().toString(),
                req.chatType(),
                req.chatId(),
                req.senderId(),
                req.text(),
                Instant.now(),
                nextSeq
        );
        return repository.save(message);
    }

    /**
     * Курсорная выдача истории: новейшие сверху, один запрос (без N+1).
     * beforeSeq == null → последние limit сообщений; иначе строго старше курсора.
     * Прямой аналог db.chat_history.find(chatId).sort(seq desc).limit(n) со слайда 146.
     */
    @Transactional(readOnly = true)
    public HistoryResponse history(String chatId, Integer limit, Long beforeSeq) {
        int size = clampLimit(limit);
        Pageable page = PageRequest.of(0, size);

        List<Message> rows = (beforeSeq == null)
                ? repository.findByChatIdOrderBySeqDesc(chatId, page)
                : repository.findByChatIdAndSeqLessThanOrderBySeqDesc(chatId, beforeSeq, page);

        List<MessageView> messages = rows.stream().map(MessageView::from).toList();
        Long nextBeforeSeq = rows.isEmpty() ? null : rows.get(rows.size() - 1).getSeq();
        boolean hasMore = rows.size() == size;

        return new HistoryResponse(chatId, messages, nextBeforeSeq, hasMore);
    }

    private int clampLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        return Math.min(Math.max(limit, 1), MAX_LIMIT);
    }
}
