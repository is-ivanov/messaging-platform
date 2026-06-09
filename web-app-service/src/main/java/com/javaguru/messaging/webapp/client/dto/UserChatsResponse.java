package com.javaguru.messaging.webapp.client.dto;

import java.util.List;

/** Ответ Groups&Channels {@code GET /api/users/{userId}/chats}: группы и каналы пользователя. */
public record UserChatsResponse(
        List<String> groupIds,
        List<ChannelInfo> channels
) {
}
