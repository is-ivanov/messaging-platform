package com.javaguru.messaging.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства самого инстанса (префикс {@code app}).
 *
 * @param serverUrl адрес, по которому Connection Management достучится до этого инстанса
 *                  для доставки (например {@code http://localhost:8084})
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String serverUrl
) {
}
