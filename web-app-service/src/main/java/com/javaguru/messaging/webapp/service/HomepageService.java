package com.javaguru.messaging.webapp.service;

import com.javaguru.messaging.webapp.client.ChatHistoryClient;
import com.javaguru.messaging.webapp.client.GroupsChannelsClient;
import com.javaguru.messaging.webapp.client.dto.ChannelInfo;
import com.javaguru.messaging.webapp.client.dto.HistoryResponse;
import com.javaguru.messaging.webapp.client.dto.UserChatsResponse;
import com.javaguru.messaging.webapp.web.dto.ChatPreview;
import com.javaguru.messaging.webapp.web.dto.HomepageResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборка homepage (слайды 100–104): список чатов пользователя + превью последних сообщений.
 *
 * <p>Это механизм «офлайн-пользователь получает пропущенное при возврате» — через историю,
 * а не через гарантированную доставку в момент отправки.
 */
@Service
public class HomepageService {

    private final GroupsChannelsClient groups;
    private final ChatHistoryClient chatHistory;

    public HomepageService(GroupsChannelsClient groups, ChatHistoryClient chatHistory) {
        this.groups = groups;
        this.chatHistory = chatHistory;
    }

    public HomepageResponse build(String userId, int previewLimit) {
        UserChatsResponse chats = groups.userChats(userId);
        List<ChatPreview> previews = new ArrayList<>();

        // Превью на чат — отдельный вызов в Chat History.
        // TODO: NFR — это N+1 по числу чатов; на нагрузке кешировать/батчить (слайды 143, 150).
        for (String groupId : chats.groupIds()) {
            HistoryResponse history = chatHistory.recent(groupId, previewLimit);
            previews.add(new ChatPreview(groupId, "GROUP", null, history.messages()));
        }
        for (ChannelInfo channel : chats.channels()) {
            HistoryResponse history = chatHistory.recent(channel.channelId(), previewLimit);
            previews.add(new ChatPreview(channel.channelId(), "CHANNEL", channel.channelName(), history.messages()));
        }

        return new HomepageResponse(userId, previews);
    }
}
