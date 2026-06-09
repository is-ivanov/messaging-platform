package com.javaguru.messaging.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Базовые URL зависимых сервисов (префикс {@code services}). */
@ConfigurationProperties(prefix = "services")
public record ServicesProperties(
        String usersUrl,
        String connectionManagementUrl
) {
}
