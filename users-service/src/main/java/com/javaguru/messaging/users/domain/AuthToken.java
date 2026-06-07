package com.javaguru.messaging.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Непрозрачный токен (UUID). Хранится в БД, чтобы другие сервисы могли
 * валидировать его через Users Service (POST /internal/auth/validate).
 */
@Entity
@Table(
        name = "auth_tokens",
        indexes = @Index(name = "idx_auth_tokens_user", columnList = "userId")
)
public class AuthToken {

    @Id
    private String token;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    protected AuthToken() {
        // for JPA
    }

    public AuthToken(String token, String userId, Instant createdAt, Instant expiresAt) {
        this.token = token;
        this.userId = userId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }
}
