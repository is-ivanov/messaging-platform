package com.javaguru.messaging.users.error;

import org.springframework.http.HttpStatus;

/**
 * Базовое доменное исключение с привязанным HTTP-статусом и кодом ошибки.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
