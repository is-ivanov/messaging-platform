package com.javaguru.messaging.users.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Регистрация. Имя — раздельно first_name / last_name (таблица users, слайд 59).
 * profileImage — необязательная строка-URL (сохраняется в profile_url; медиа out of scope).
 */
public record SignupRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 6, max = 100) String password,
        String profileImage
) {
}
