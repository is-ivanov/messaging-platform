package com.javaguru.messaging.connection.client.dto;

/** Ответ Groups&Channels на resolve: id существующей или только что созданной группы. */
public record ResolveGroupResponse(
        String groupId,
        boolean created
) {
}
