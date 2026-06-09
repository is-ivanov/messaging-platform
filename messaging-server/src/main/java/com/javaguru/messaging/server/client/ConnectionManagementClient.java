package com.javaguru.messaging.server.client;

import com.javaguru.messaging.server.client.dto.RegisterConnectionRequest;
import com.javaguru.messaging.server.client.dto.RouteRequest;
import com.javaguru.messaging.server.client.dto.RouteResponse;
import com.javaguru.messaging.server.config.ServicesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Клиент Connection Management (8090): реестр соединений + маршрутизация. */
@Component
public class ConnectionManagementClient {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManagementClient.class);

    private final RestClient http;
    private final String baseUrl;

    public ConnectionManagementClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.connectionManagementUrl();
    }

    /** Зарегистрировать соединение (на connect). Best-effort: connect не должен падать из-за CM. */
    public void register(String userId, String serverUrl) {
        try {
            http.put()
                    .uri(baseUrl + "/internal/connections/{id}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RegisterConnectionRequest(serverUrl))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Регистрация соединения userId={} в CM не удалась: {}", userId, e.toString());
        }
    }

    /** Снять соединение (на disconnect). Best-effort. */
    public void deregister(String userId) {
        try {
            http.delete()
                    .uri(baseUrl + "/internal/connections/{id}", userId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Снятие соединения userId={} в CM не удалось: {}", userId, e.toString());
        }
    }

    /** Отправить сообщение на маршрутизацию. Ошибки пробрасываем — отправителю уйдёт ERROR. */
    public RouteResponse route(RouteRequest request) {
        return http.post()
                .uri(baseUrl + "/internal/messages/route")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(RouteResponse.class);
    }
}
