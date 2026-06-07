package com.javaguru.messaging.groups.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Таблица Groups (слайд 71): group_id, timestamp.
 * На слайде у группы НЕТ имени — только идентификатор и время создания.
 * Личный чат 1-на-1 моделируется как группа из двух участников (API: sendMessage(u1,u2) -> groupChatId).
 *
 * Класс назван GroupChat, т.к. "Group" — служебное слово; таблица — `groups` (заквочена для H2).
 */
@Entity
@Table(name = "`groups`")
public class GroupChat {

    @Id
    @Column(name = "group_id")
    private String id;

    /** На слайде колонка называется `timestamp`; это время создания группы. */
    @Column(name = "`timestamp`", nullable = false)
    private Instant timestamp;

    protected GroupChat() {
        // for JPA
    }

    public GroupChat(String id, Instant timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
