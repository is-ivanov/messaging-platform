package com.javaguru.messaging.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Базовые URL зависимых сервисов (префикс {@code services}). */
@ConfigurationProperties(prefix = "services")
public record ServicesProperties(
        String groupsChannelsUrl,
        String chatHistoryUrl
) {
}
