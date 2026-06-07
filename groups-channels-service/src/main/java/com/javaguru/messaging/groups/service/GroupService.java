package com.javaguru.messaging.groups.service;

import com.javaguru.messaging.groups.domain.GroupChat;
import com.javaguru.messaging.groups.domain.GroupParticipant;
import com.javaguru.messaging.groups.error.NotFoundException;
import com.javaguru.messaging.groups.repository.GroupChatRepository;
import com.javaguru.messaging.groups.repository.GroupParticipantRepository;
import com.javaguru.messaging.groups.web.dto.ResolveGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupChatRepository groups;
    private final GroupParticipantRepository participants;

    public GroupService(GroupChatRepository groups, GroupParticipantRepository participants) {
        this.groups = groups;
        this.participants = participants;
    }

    /**
     * Lazy-resolve: вернуть существующую группу с таким же набором участников
     * или создать новую. Применяется и для приватного чата 1-на-1 (2 участника),
     * и для группового (N участников). Повторные сообщения тем же людям
     * переиспользуют один и тот же чат.
     */
    @Transactional
    public ResolveGroupResponse resolve(List<String> rawParticipantIds) {
        List<String> ids = rawParticipantIds.stream().distinct().sorted().toList();

        List<String> existing = participants.findGroupIdsWithExactParticipants(ids, ids.size());
        if (!existing.isEmpty()) {
            return new ResolveGroupResponse(existing.get(0), false);
        }

        String groupId = UUID.randomUUID().toString();
        groups.save(new GroupChat(groupId, Instant.now()));
        for (String userId : ids) {
            participants.save(new GroupParticipant(groupId, userId));
        }
        return new ResolveGroupResponse(groupId, true);
    }

    /** Список userId участников группы. Бросает 404, если группы нет. */
    @Transactional(readOnly = true)
    public List<String> getMembers(String groupId) {
        if (!groups.existsById(groupId)) {
            throw new NotFoundException("GROUP_NOT_FOUND", "Group not found: " + groupId);
        }
        return participants.findByGroupId(groupId).stream()
                .map(GroupParticipant::getUserId)
                .toList();
    }

    /** Группы, в которых состоит пользователь (для homepage). */
    @Transactional(readOnly = true)
    public List<String> getUserGroupIds(String userId) {
        return participants.findGroupIdsByUserId(userId);
    }
}
