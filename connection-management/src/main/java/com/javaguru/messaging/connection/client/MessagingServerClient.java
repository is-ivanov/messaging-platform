package com.javaguru.messaging.connection.client;

import com.javaguru.messaging.connection.client.dto.DeliverRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Клиент тонкого Messaging Server: доставка сообщения локально подключённому получателю.
 * Адрес сервера ({@code serverUrl}) динамический — берётся из реестра соединений на каждый вызов.
 *
 * <p>// TODO: NFR — pub/sub: прямой REST между серверами заменить на брокер (слайды 132–134).
 */
@Component
public class MessagingServerClient {

    private final RestClient http;

    public MessagingServerClient(RestClient http) {
        this.http = http;
    }

    /** Доставить MESSAGE на конкретный Messaging Server (тот, где висит сокет получателя). */
    public void deliver(String serverUrl, DeliverRequest request) {
        http.post()
                .uri(serverUrl + "/internal/deliver")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
