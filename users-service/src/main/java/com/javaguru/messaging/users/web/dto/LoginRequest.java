package com.javaguru.messaging.users.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * login(username, password) -> [authToken, userId]
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
