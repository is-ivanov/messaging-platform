package com.javaguru.messaging.connection.service;

import com.javaguru.messaging.connection.client.ChatHistoryClient;
import com.javaguru.messaging.connection.client.GroupsChannelsClient;
import com.javaguru.messaging.connection.client.MessagingServerClient;
import com.javaguru.messaging.connection.client.dto.DeliverRequest;
import com.javaguru.messaging.connection.client.dto.StoreMessageRequest;
import com.javaguru.messaging.connection.client.dto.StoredMessage;
import com.javaguru.messaging.connection.domain.ChatType;
import com.javaguru.messaging.connection.domain.ConnectionRegistry;
import com.javaguru.messaging.connection.error.BadRequestException;
import com.javaguru.messaging.connection.web.dto.RouteRequest;
import com.javaguru.messaging.connection.web.dto.RouteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Оркестрация доставки — логика, «убранная из messaging service» (слайд 133).
 *
 * <p>Шаги (слайды 124–128): resolve chatId → store в Chat History → получатели из Groups&Channels →
 * маршрутизация по KV-реестру → fan-out онлайн-получателям → ACK отправителю.
 */
@Service
public class RoutingService {

    private static final Logger log = LoggerFactory.getLogger(RoutingService.class);

    private final ConnectionRegistry registry;
    private final ChatHistoryClient chatHistory;
    private final GroupsChannelsClient groups;
    private final MessagingServerClient messaging;

    public RoutingService(ConnectionRegistry registry,
                          ChatHistoryClient chatHistory,
                          GroupsChannelsClient groups,
                          MessagingServerClient messaging) {
        this.registry = registry;
        this.chatHistory = chatHistory;
        this.groups = groups;
        this.messaging = messaging;
    }

    public RouteResponse route(RouteRequest req) {
        // 1. Определить chatId и тип чата.
        ChatType chatType;
        String chatId;
        switch (req.type()) {
            case SEND_DIRECT -> {
                if (isBlank(req.toUserId())) {
                    throw new BadRequestException("MISSING_TO_USER", "SEND_DIRECT требует toUserId");
                }
                chatType = ChatType.GROUP; // личный чат = группа из двух
                chatId = groups.resolveGroup(List.of(req.senderId(), req.toUserId())); // lazy
            }
            case SEND_GROUP -> {
                requireChatId(req);
                chatType = ChatType.GROUP;
                chatId = req.chatId();
            }
            case SEND_CHANNEL -> {
                requireChatId(req);
                chatType = ChatType.CHANNEL;
                chatId = req.chatId();
            }
            default -> throw new BadRequestException("BAD_TYPE", "неизвестный type: " + req.type());
        }

        // 2. Сохранить сообщение → получить seq/timestamp/messageId.
        StoredMessage stored = chatHistory.store(
                new StoreMessageRequest(chatType, chatId, req.senderId(), req.text()));

        // 3. Получатели.
        List<String> recipients = (chatType == ChatType.GROUP)
                ? groups.groupMembers(chatId)
                : groups.channelSubscribers(chatId);

        // 4. Fan-out онлайн-получателям (кроме отправителя — ему ACK, не MESSAGE).
        // TODO: NFR — pub/sub: на нескольких серверах эта рассылка уходит за брокер (слайды 132–134).
        List<String> deliveredTo = new ArrayList<>();
        List<String> offline = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        for (String userId : recipients) {
            if (userId.equals(req.senderId())) {
                continue;
            }
            Optional<String> serverUrl = registry.serverOf(userId);
            if (serverUrl.isEmpty()) {
                offline.add(userId); // не в сети — заберёт историей при входе
                continue;
            }
            try {
                messaging.deliver(serverUrl.get(), new DeliverRequest(
                        userId, chatId, chatType, req.senderId(), req.text(),
                        stored.timestamp(), stored.seq()));
                deliveredTo.add(userId);
            } catch (Exception e) {
                log.warn("Доставка userId={} на {} не удалась: {}", userId, serverUrl.get(), e.toString());
                failed.add(userId);
            }
        }

        int recipientCount = (int) recipients.stream().filter(id -> !id.equals(req.senderId())).count();
        return new RouteResponse(chatId, chatType, stored.messageId(), stored.seq(), stored.timestamp(),
                recipientCount, deliveredTo, offline, failed);
    }

    private void requireChatId(RouteRequest req) {
        if (isBlank(req.chatId())) {
            throw new BadRequestException("MISSING_CHAT_ID", req.type() + " требует chatId");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
