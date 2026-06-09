package com.javaguru.messaging.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Маршруты шлюза. Порядок важен: более специфичный путь должен идти раньше — поэтому
 * путь chats у пользователя (это Groups&Channels) объявлен ДО общего {@code /api/users/} (Users).
 */
@Configuration
@EnableConfigurationProperties(RoutesProperties.class)
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, RoutesProperties props) {
        return builder.routes()
                // homepage пользователя живёт в Groups&Channels — ловим ПЕРЕД общим /api/users/**
                .route("users-chats", r -> r.path("/api/users/*/chats").uri(props.groupsChannelsUrl()))
                .route("users", r -> r.path("/api/users/**").uri(props.usersUrl()))
                .route("groups-channels", r -> r.path("/api/groups/**", "/api/channels/**").uri(props.groupsChannelsUrl()))
                .route("chat-history", r -> r.path("/api/chats/**").uri(props.chatHistoryUrl()))
                // web-app: homepage-агрегатор + статичная страница теста WS
                .route("web-app", r -> r.path("/api/homepage", "/", "/index.html", "/static/**", "/favicon.ico").uri(props.webAppUrl()))
                // WebSocket в тонкий Messaging Server (ws:// проксирует только реактивный gateway)
                .route("messaging-ws", r -> r.path("/ws", "/ws/**").uri(props.messagingServerUrl()))
                .build();
    }
}
