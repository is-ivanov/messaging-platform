package com.javaguru.messaging.users.service;

import com.javaguru.messaging.users.domain.AuthToken;
import com.javaguru.messaging.users.error.InvalidTokenException;
import com.javaguru.messaging.users.repository.AuthTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class TokenService {

    /** Срок жизни токена. Для учебного проекта — фиксированные 7 дней. */
    private static final Duration TTL = Duration.ofDays(7);

    private final AuthTokenRepository tokens;

    public TokenService(AuthTokenRepository tokens) {
        this.tokens = tokens;
    }

    @Transactional
    public AuthToken issue(String userId) {
        Instant now = Instant.now();
        AuthToken token = new AuthToken(
                UUID.randomUUID().toString(),
                userId,
                now,
                now.plus(TTL)
        );
        return tokens.save(token);
    }

    /**
     * Возвращает userId по валидному токену.
     * Бросает {@link InvalidTokenException}, если токен не найден или истёк.
     */
    @Transactional(readOnly = true)
    public String validate(String token) {
        AuthToken found = tokens.findById(token)
                .orElseThrow(InvalidTokenException::new);
        if (found.isExpired(Instant.now())) {
            throw new InvalidTokenException();
        }
        return found.getUserId();
    }
}
