package com.javaguru.messaging.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Адреса бэкендов (префикс {@code services}). */
@ConfigurationProperties(prefix = "services")
public record RoutesProperties(
        String usersUrl,
        String groupsChannelsUrl,
        String chatHistoryUrl,
        String webAppUrl,
        String messagingServerUrl
) {
}
