package com.javaguru.messaging.connection.client;

import com.javaguru.messaging.connection.client.dto.StoreMessageRequest;
import com.javaguru.messaging.connection.client.dto.StoredMessage;
import com.javaguru.messaging.connection.config.ServicesProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Клиент Chat History Service (8083): сохранение сообщения в историю. */
@Component
public class ChatHistoryClient {

    private final RestClient http;
    private final String baseUrl;

    public ChatHistoryClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.chatHistoryUrl();
    }

    /** Сохранить сообщение → получить присвоенные messageId, seq, timestamp. */
    public StoredMessage store(StoreMessageRequest request) {
        return http.post()
                .uri(baseUrl + "/internal/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(StoredMessage.class);
    }
}
