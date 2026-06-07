package com.javaguru.messaging.groups.web.dto;

import java.util.List;

/**
 * Чаты пользователя для homepage (слайд 101): его группы и каналы.
 */
public record UserChatsResponse(
        List<String> groupIds,
        List<ChannelResponse> channels
) {
}
