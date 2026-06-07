package com.javaguru.messaging.groups.web.dto;

/**
 * joinChannel(userId, channelId) -> Confirmation.
 */
public record JoinChannelResponse(
        String channelId,
        String userId,
        boolean joined
) {
}
