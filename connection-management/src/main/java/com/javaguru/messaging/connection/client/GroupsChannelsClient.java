package com.javaguru.messaging.connection.client;

import com.javaguru.messaging.connection.client.dto.MembersResponse;
import com.javaguru.messaging.connection.client.dto.ResolveGroupRequest;
import com.javaguru.messaging.connection.client.dto.ResolveGroupResponse;
import com.javaguru.messaging.connection.config.ServicesProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/** Клиент Groups & Channels Service (8082): resolve группы и список получателей. */
@Component
public class GroupsChannelsClient {

    private final RestClient http;
    private final String baseUrl;

    public GroupsChannelsClient(RestClient http, ServicesProperties props) {
        this.http = http;
        this.baseUrl = props.groupsChannelsUrl();
    }

    /** Lazy-resolve группы по участникам (для 1-на-1 — {@code [senderId, toUserId]}). Возвращает groupId. */
    public String resolveGroup(List<String> participantIds) {
        ResolveGroupResponse response = http.post()
                .uri(baseUrl + "/internal/groups/resolve")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResolveGroupRequest(participantIds))
                .retrieve()
                .body(ResolveGroupResponse.class);
        return response.groupId();
    }

    /** Участники группы (кому доставлять). */
    public List<String> groupMembers(String groupId) {
        MembersResponse response = http.get()
                .uri(baseUrl + "/internal/groups/{id}/members", groupId)
                .retrieve()
                .body(MembersResponse.class);
        return response.userIds();
    }

    /** Подписчики канала (кому доставлять). */
    public List<String> channelSubscribers(String channelId) {
        MembersResponse response = http.get()
                .uri(baseUrl + "/internal/channels/{id}/subscribers", channelId)
                .retrieve()
                .body(MembersResponse.class);
        return response.userIds();
    }
}
