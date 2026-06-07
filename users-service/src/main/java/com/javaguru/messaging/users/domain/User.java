package com.javaguru.messaging.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Таблица users (слайд 59): user_id, username, first_name, last_name, profile_url, ...
 * Имя пользователя хранится раздельно (first_name / last_name), как на слайде —
 * в отличие от единого fullName из API-сигнатуры на слайде 29.
 * Поля password_hash, status, created_at — наше дополнение (на слайде колонка "...").
 */
@Entity
@Table(
        name = "users",
        indexes = @Index(name = "idx_users_username", columnList = "username", unique = true)
)
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** profile_url (слайд 59). По требованиям только текст — строка-URL, без Object Store. */
    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected User() {
        // for JPA
    }

    public User(String id, String username, String firstName, String lastName, String passwordHash,
                String profileUrl, UserStatus status, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.profileUrl = profileUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
