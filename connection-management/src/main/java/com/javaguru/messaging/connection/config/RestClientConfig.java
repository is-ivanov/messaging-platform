package com.javaguru.messaging.connection.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Один общий {@link RestClient} для всех меж-сервисных вызовов.
 * Базовые URL не зашиваем в клиент — Messaging Server-ы адресуются динамически
 * (их URL берётся из реестра соединений), поэтому строим полный URL на каждый вызов.
 */
@Configuration
@EnableConfigurationProperties(ServicesProperties.class)
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
