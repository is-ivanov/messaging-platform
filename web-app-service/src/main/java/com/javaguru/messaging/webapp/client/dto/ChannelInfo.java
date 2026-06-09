package com.javaguru.messaging.webapp.client.dto;

/** Канал из ответа Groups&Channels {@code /api/users/{id}/chats}. */
public record ChannelInfo(
        String channelId,
        String channelName,
        String channelUrl,
        String ownerId
) {
}
