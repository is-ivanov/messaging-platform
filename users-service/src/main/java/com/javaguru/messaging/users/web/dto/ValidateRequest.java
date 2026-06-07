package com.javaguru.messaging.users.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidateRequest(
        @NotBlank String token
) {
}
