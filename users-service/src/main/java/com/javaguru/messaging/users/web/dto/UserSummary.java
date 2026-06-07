package com.javaguru.messaging.users.web.dto;

import com.javaguru.messaging.users.domain.User;
import com.javaguru.messaging.users.domain.UserStatus;

/**
 * Публичное представление пользователя (без password_hash).
 * Имя раздельно first_name / last_name (таблица users, слайд 59).
 */
public record UserSummary(
        String userId,
        String username,
        String firstName,
        String lastName,
        String profileUrl,
        UserStatus status
) {
    public static UserSummary from(User user) {
        return new UserSummary(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileUrl(),
                user.getStatus()
        );
    }
}
