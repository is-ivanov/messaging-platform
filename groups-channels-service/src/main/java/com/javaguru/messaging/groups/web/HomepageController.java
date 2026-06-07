package com.javaguru.messaging.groups.web;

import com.javaguru.messaging.groups.service.ChannelService;
import com.javaguru.messaging.groups.service.GroupService;
import com.javaguru.messaging.groups.web.dto.ChannelResponse;
import com.javaguru.messaging.groups.web.dto.UserChatsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Чаты пользователя для homepage (слайд 101): его группы и каналы.
 */
@RestController
@RequestMapping("/api/users")
public class HomepageController {

    private final GroupService groupService;
    private final ChannelService channelService;

    public HomepageController(GroupService groupService, ChannelService channelService) {
        this.groupService = groupService;
        this.channelService = channelService;
    }

    @GetMapping("/{userId}/chats")
    public UserChatsResponse getUserChats(@PathVariable String userId) {
        return new UserChatsResponse(
                groupService.getUserGroupIds(userId),
                channelService.getUserChannels(userId).stream()
                        .map(ChannelResponse::from)
                        .toList()
        );
    }
}
