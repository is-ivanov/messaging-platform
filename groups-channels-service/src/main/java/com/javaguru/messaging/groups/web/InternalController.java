package com.javaguru.messaging.groups.web;

import com.javaguru.messaging.groups.service.ChannelService;
import com.javaguru.messaging.groups.service.GroupService;
import com.javaguru.messaging.groups.web.dto.MembersResponse;
import com.javaguru.messaging.groups.web.dto.ResolveGroupRequest;
import com.javaguru.messaging.groups.web.dto.ResolveGroupResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Внутренний API для Messaging Service:
 * - lazy-resolve группы по участникам (для sendMessage 1-на-1 / группа);
 * - получить получателей сообщения (участники группы / подписчики канала).
 */
@RestController
@RequestMapping("/internal")
public class InternalController {

    private final GroupService groupService;
    private final ChannelService channelService;

    public InternalController(GroupService groupService, ChannelService channelService) {
        this.groupService = groupService;
        this.channelService = channelService;
    }

    @PostMapping("/groups/resolve")
    public ResolveGroupResponse resolveGroup(@Valid @RequestBody ResolveGroupRequest request) {
        return groupService.resolve(request.participantIds());
    }

    @GetMapping("/groups/{groupId}/members")
    public MembersResponse groupMembers(@PathVariable String groupId) {
        return new MembersResponse(groupId, groupService.getMembers(groupId));
    }

    @GetMapping("/channels/{channelId}/subscribers")
    public MembersResponse channelSubscribers(@PathVariable String channelId) {
        return new MembersResponse(channelId, channelService.getSubscribers(channelId));
    }
}
