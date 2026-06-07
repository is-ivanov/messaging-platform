package com.javaguru.messaging.chathistory.web;

import com.javaguru.messaging.chathistory.service.ChatHistoryService;
import com.javaguru.messaging.chathistory.web.dto.MessageView;
import com.javaguru.messaging.chathistory.web.dto.StoreMessageRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Внутренний API для Messaging Service: сохранение сообщения в историю.
 * Возвращает присвоенные message_id, seq и timestamp (нужны оркестратору для рассылки MESSAGE/ACK).
 */
@RestController
@RequestMapping("/internal")
public class InternalController {

    private final ChatHistoryService service;

    public InternalController(ChatHistoryService service) {
        this.service = service;
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageView store(@Valid @RequestBody StoreMessageRequest request) {
        return MessageView.from(service.store(request));
    }
}
