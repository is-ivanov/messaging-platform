package com.javaguru.messaging.connection.client.dto;

import java.util.List;

/** Тело запроса в Groups&Channels: {@code POST /internal/groups/resolve} (lazy-создание 1-на-1/группы). */
public record ResolveGroupRequest(
        List<String> participantIds
) {
}
