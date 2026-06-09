package com.javaguru.messaging.connection.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Базовые URL зависимых сервисов (префикс {@code services} в application.yml).
 */
@ConfigurationProperties(prefix = "services")
public record ServicesProperties(
        String usersUrl,
        String groupsChannelsUrl,
        String chatHistoryUrl
) {
}
