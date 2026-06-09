package com.javaguru.messaging.connection.client.dto;

import java.util.List;

/** Ответ Groups&Channels: список userId участников группы / подписчиков канала. */
public record MembersResponse(
        String chatId,
        List<String> userIds
) {
}
