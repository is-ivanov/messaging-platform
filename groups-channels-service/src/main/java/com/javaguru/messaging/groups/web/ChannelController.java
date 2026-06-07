package com.javaguru.messaging.groups.web;

import com.javaguru.messaging.groups.service.ChannelService;
import com.javaguru.messaging.groups.web.dto.ChannelResponse;
import com.javaguru.messaging.groups.web.dto.CreateChannelRequest;
import com.javaguru.messaging.groups.web.dto.JoinChannelResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Внешний API каналов (слайды 21–25, 90–92).
 * Личность вызывающего — из заголовка X-User-Id (имитация auth-контекста за шлюзом),
 * а не из тела запроса (конвенция курса: identity не из payload).
 */
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelResponse createChannel(@RequestHeader("X-User-Id") String ownerId,
                                         @Valid @RequestBody CreateChannelRequest request) {
        return ChannelResponse.from(channelService.create(ownerId, request.channelName()));
    }

    @PostMapping("/{channelId}/join")
    public JoinChannelResponse joinChannel(@RequestHeader("X-User-Id") String userId,
                                           @PathVariable String channelId) {
        return channelService.join(channelId, userId);
    }
}
