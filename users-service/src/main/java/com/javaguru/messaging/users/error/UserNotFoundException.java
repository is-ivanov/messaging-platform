package com.javaguru.messaging.users.error;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String key) {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found: " + key);
    }
}
