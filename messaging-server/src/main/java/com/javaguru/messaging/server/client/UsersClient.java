package com.javaguru.messaging.server.client;

import com.javaguru.messaging.server.client.dto.ValidateRequest;
import com.javaguru.messaging.server.client.dto.ValidateResponse;
import com.javaguru.messaging.server.config.ServicesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Клиент Users Service (8081): валидация токена при WebSocket-handshake. */
@Component
public class UsersClient {

    private static final Logger log = LoggerFactory.getLogger(UsersClient.class);

    private final RestClient http;
    private final String baseUrl;

    public UsersClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.usersUrl();
    }

    /** Вернуть userId по токену, либо null, если токен невалиден/истёк или Users недоступен. */
    public String validateToken(String token) {
        try {
            ValidateResponse response = http.post()
                    .uri(baseUrl + "/internal/auth/validate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ValidateRequest(token))
                    .retrieve()
                    .body(ValidateResponse.class);
            return response == null ? null : response.userId();
        } catch (Exception e) {
            log.warn("Валидация токена не удалась: {}", e.toString());
            return null;
        }
    }
}
