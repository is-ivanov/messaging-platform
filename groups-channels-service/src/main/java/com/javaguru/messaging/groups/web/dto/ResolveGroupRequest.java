package com.javaguru.messaging.groups.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Lazy-resolve группы по набору участников (вызывает Messaging Service при sendMessage).
 * Для 1-на-1 participantIds = [userA, userB]. Минимум 2 участника.
 */
public record ResolveGroupRequest(
        @NotNull @Size(min = 2, message = "группа должна содержать минимум 2 участника") List<String> participantIds
) {
}
