package com.javaguru.messaging.server.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Один общий {@link RestClient} для вызовов Users и Connection Management.
 * Через {@code RestClient.create()} — в Spring Boot 4.0 бин {@code RestClient.Builder}
 * не авто-конфигурируется.
 */
@Configuration
@EnableConfigurationProperties({AppProperties.class, ServicesProperties.class})
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
