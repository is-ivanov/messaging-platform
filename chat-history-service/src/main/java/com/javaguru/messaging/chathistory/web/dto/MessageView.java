package com.javaguru.messaging.chathistory.web.dto;

import com.javaguru.messaging.chathistory.domain.ChatType;
import com.javaguru.messaging.chathistory.domain.Message;

import java.time.Instant;

/** Представление сохранённого сообщения (ответ store + элемент истории). */
public record MessageView(
        String messageId,
        ChatType chatType,
        String chatId,
        String senderId,
        String text,
        Instant timestamp,
        long seq
) {
    public static MessageView from(Message m) {
        return new MessageView(
                m.getId(),
                m.getChatType(),
                m.getChatId(),
                m.getSenderId(),
                m.getText(),
                m.getTimestamp(),
                m.getSeq()
        );
    }
}
