package com.javaguru.messaging.users.error;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException(String username) {
        super(HttpStatus.CONFLICT, "USERNAME_TAKEN",
                "Username already exists: " + username);
    }
}
