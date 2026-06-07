package com.javaguru.messaging.groups.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Таблица Group_Participants (слайд 72): user_id, group_id.
 * На слайде ключ составной (group_id, user_id); здесь для простоты JPA добавлен
 * суррогатный id + уникальное ограничение на пару — поведение то же.
 */
@Entity
@Table(
        name = "group_participants",
        uniqueConstraints = @UniqueConstraint(name = "uq_group_user", columnNames = {"group_id", "user_id"})
)
public class GroupParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    protected GroupParticipant() {
        // for JPA
    }

    public GroupParticipant(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }
}
