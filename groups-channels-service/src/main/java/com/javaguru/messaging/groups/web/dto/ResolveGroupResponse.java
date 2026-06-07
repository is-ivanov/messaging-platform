package com.javaguru.messaging.groups.web.dto;

/**
 * groupId существующей или только что созданной группы.
 * created=true, если группа была создана этим вызовом (lazy creation).
 */
public record ResolveGroupResponse(
        String groupId,
        boolean created
) {
}
