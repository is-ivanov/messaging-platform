package com.javaguru.messaging.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway (порт 8080) — единая точка входа (слайды 137–139, 150).
 *
 * <p>На реактивном Spring Cloud Gateway (webflux) — он умеет проксировать и HTTP, и WebSocket.
 * Две задачи:
 * <ul>
 *   <li>маршрутизация: один публичный порт → нужный бэкенд по пути;</li>
 *   <li>аутентификация: валидирует токен через Users и проставляет вниз {@code X-User-Id}
 *       (бэкенды доверяют этому заголовку — закрывает открытый вопрос §9.1).</li>
 * </ul>
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
