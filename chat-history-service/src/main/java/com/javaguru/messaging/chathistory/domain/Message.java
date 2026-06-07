package com.javaguru.messaging.chathistory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

/**
 * Документ Chat_History (слайд 78): message_id, group_id | channel_id, sender_id, timestamp, text.
 *
 * Модель данных:
 * - {@code group_id | channel_id} со слайда сведены к паре ({@link ChatType} chatType, chatId) —
 *   единый ключ партиции (вариант из §4.3 плана). chatId = идентификатор группы ИЛИ канала.
 * - {@code seq} — наше добавление: монотонный счётчик per-chat для курсорной пагинации без N+1.
 *   Compound-индекс {@code (chat_id, seq)} — прямой аналог слайдов 144–146
 *   {@code (hash(group_id), sorted(message_id))} / {@code (hash(channel_id), sorted(message_id))}.
 */
@Entity
@Table(
        name = "chat_history",
        uniqueConstraints = @UniqueConstraint(name = "uq_chat_seq", columnNames = {"chat_id", "seq"}),
        indexes = @Index(name = "idx_chat_seq", columnList = "chat_id, seq")
)
public class Message {

    @Id
    @Column(name = "message_id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type", nullable = false)
    private ChatType chatType;

    /** Идентификатор группы или канала (см. {@link ChatType}). Ключ партиции истории. */
    @Column(name = "chat_id", nullable = false)
    private String chatId;

    /** Автор сообщения. Передаётся доверенным Messaging Service из auth-контекста, не из клиентского payload. */
    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "text", nullable = false, length = 10_000)
    private String text;

    /** На слайде колонка называется timestamp; заквочена для H2 (зарезервированное слово). */
    @Column(name = "`timestamp`", nullable = false)
    private Instant timestamp;

    /** Монотонный per-chat номер сообщения; курсор для пагинации. */
    @Column(name = "seq", nullable = false)
    private long seq;

    protected Message() {
        // for JPA
    }

    public Message(String id, ChatType chatType, String chatId, String senderId,
                   String text, Instant timestamp, long seq) {
        this.id = id;
        this.chatType = chatType;
        this.chatId = chatId;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public String getChatId() {
        return chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public long getSeq() {
        return seq;
    }
}
