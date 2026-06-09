package com.javaguru.messaging.webapp.client;

import com.javaguru.messaging.webapp.client.dto.HistoryResponse;
import com.javaguru.messaging.webapp.config.ServicesProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Клиент Chat History (8083): превью последних сообщений по чату. */
@Component
public class ChatHistoryClient {

    private final RestClient http;
    private final String baseUrl;

    public ChatHistoryClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.chatHistoryUrl();
    }

    public HistoryResponse recent(String chatId, int limit) {
        return http.get()
                .uri(baseUrl + "/api/chats/{id}/messages?limit={limit}", chatId, limit)
                .retrieve()
                .body(HistoryResponse.class);
    }
}
