package com.javaguru.messaging.groups.web.dto;

import java.util.List;

/**
 * Список userId участников группы / подписчиков канала
 * (слайды 87–88, 95–96 — "кому доставлять сообщение").
 */
public record MembersResponse(
        String chatId,
        List<String> userIds
) {
}
