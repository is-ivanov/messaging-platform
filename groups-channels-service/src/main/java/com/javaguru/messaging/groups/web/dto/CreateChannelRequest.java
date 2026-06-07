package com.javaguru.messaging.groups.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * createChannel(channelName) -> [channelId, channelUrl]. Владелец берётся из X-User-Id.
 */
public record CreateChannelRequest(
        @NotBlank @Size(max = 100) String channelName
) {
}
