package com.javaguru.messaging.groups.web.dto;

import com.javaguru.messaging.groups.domain.Channel;

public record ChannelResponse(
        String channelId,
        String channelName,
        String channelUrl,
        String ownerId
) {
    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelUrl(),
                channel.getOwnerId()
        );
    }
}
