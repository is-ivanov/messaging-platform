package com.javaguru.messaging.connection.client;

import com.javaguru.messaging.connection.client.dto.StatusUpdateRequest;
import com.javaguru.messaging.connection.config.ServicesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Клиент Users Service (8081): обновление presence-статуса ONLINE/OFFLINE. */
@Component
public class UsersClient {

    private static final Logger log = LoggerFactory.getLogger(UsersClient.class);

    private final RestClient http;
    private final String baseUrl;

    public UsersClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.usersUrl();
    }

    /**
     * Пометить пользователя ONLINE/OFFLINE. Best-effort: если Users недоступен,
     * соединение всё равно регистрируется/снимается — presence не должен ронять connect/disconnect.
     */
    public void markStatus(String userId, String status) {
        try {
            http.post()
                    .uri(baseUrl + "/internal/users/{id}/status", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StatusUpdateRequest(status))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Не удалось обновить статус userId={} -> {}: {}", userId, status, e.toString());
        }
    }
}
