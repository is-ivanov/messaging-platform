package com.javaguru.messaging.webapp.web.dto;

import java.util.List;

/** Homepage пользователя: его чаты с превью последних сообщений (слайды 100–104). */
public record HomepageResponse(
        String userId,
        List<ChatPreview> chats
) {
}
