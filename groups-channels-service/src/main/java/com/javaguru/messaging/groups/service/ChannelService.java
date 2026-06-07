package com.javaguru.messaging.groups.service;

import com.javaguru.messaging.groups.domain.Channel;
import com.javaguru.messaging.groups.domain.ChannelSubscriber;
import com.javaguru.messaging.groups.error.NotFoundException;
import com.javaguru.messaging.groups.repository.ChannelRepository;
import com.javaguru.messaging.groups.repository.ChannelSubscriberRepository;
import com.javaguru.messaging.groups.web.dto.JoinChannelResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ChannelService {

    /** Базовый URL для генерации channel_url (слайд 74). */
    private static final String CHANNEL_URL_PREFIX = "https://chat.example.com/c/";

    private final ChannelRepository channels;
    private final ChannelSubscriberRepository subscribers;

    public ChannelService(ChannelRepository channels, ChannelSubscriberRepository subscribers) {
        this.channels = channels;
        this.subscribers = subscribers;
    }

    /** createChannel: создаёт канал и подписывает владельца. */
    @Transactional
    public Channel create(String ownerId, String channelName) {
        String channelId = UUID.randomUUID().toString();
        Channel channel = new Channel(
                channelId,
                channelName,
                Instant.now(),
                ownerId,
                CHANNEL_URL_PREFIX + channelId
        );
        channels.save(channel);
        // владелец автоматически становится подписчиком
        subscribers.save(new ChannelSubscriber(channelId, ownerId));
        return channel;
    }

    /** joinChannel: идемпотентно (повторный join не дублирует подписку). */
    @Transactional
    public JoinChannelResponse join(String channelId, String userId) {
        if (!channels.existsById(channelId)) {
            throw new NotFoundException("CHANNEL_NOT_FOUND", "Channel not found: " + channelId);
        }
        boolean already = subscribers.existsByChannelIdAndUserId(channelId, userId);
        if (!already) {
            subscribers.save(new ChannelSubscriber(channelId, userId));
        }
        return new JoinChannelResponse(channelId, userId, true);
    }

    /** Список userId подписчиков канала. Бросает 404, если канала нет. */
    @Transactional(readOnly = true)
    public List<String> getSubscribers(String channelId) {
        if (!channels.existsById(channelId)) {
            throw new NotFoundException("CHANNEL_NOT_FOUND", "Channel not found: " + channelId);
        }
        return subscribers.findByChannelId(channelId).stream()
                .map(ChannelSubscriber::getUserId)
                .toList();
    }

    /** Каналы, на которые подписан пользователь (для homepage). */
    @Transactional(readOnly = true)
    public List<Channel> getUserChannels(String userId) {
        List<String> ids = subscribers.findChannelIdsByUserId(userId);
        return ids.isEmpty() ? List.of() : channels.findByIdIn(ids);
    }
}
