package com.javaguru.messaging.connection.client.dto;

/** Тело запроса в Users: {@code POST /internal/users/{id}/status} ("ONLINE"/"OFFLINE"). */
public record StatusUpdateRequest(
        String status
) {
}
