package com.javaguru.messaging.webapp.client;

import com.javaguru.messaging.webapp.client.dto.UserChatsResponse;
import com.javaguru.messaging.webapp.config.ServicesProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Клиент Groups & Channels (8082): чаты пользователя для homepage. */
@Component
public class GroupsChannelsClient {

    private final RestClient http;
    private final String baseUrl;

    public GroupsChannelsClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.groupsChannelsUrl();
    }

    public UserChatsResponse userChats(String userId) {
        return http.get()
                .uri(baseUrl + "/api/users/{id}/chats", userId)
                .retrieve()
                .body(UserChatsResponse.class);
    }
}
