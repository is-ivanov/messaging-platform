package com.javaguru.messaging.webapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("MISSING_HEADER", ex.getHeaderName() + " обязателен (его проставляет API Gateway)"));
    }

    /** Зависимый сервис (Groups&Channels / Chat History) недоступен или вернул ошибку. */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleDownstream(RestClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse("DOWNSTREAM_ERROR", ex.getMessage()));
    }
}
