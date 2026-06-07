package com.javaguru.messaging.users.web.dto;

/**
 * Ответ для signup/login: [authToken, userId].
 */
public record AuthResponse(
        String authToken,
        String userId
) {
}
