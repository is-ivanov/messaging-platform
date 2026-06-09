package com.javaguru.messaging.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web App Service (порт 8088) — вход для клиента (слайды 60–67, 100–104).
 *
 * <p>Делает две вещи:
 * <ul>
 *   <li>агрегирует homepage: чаты пользователя (Groups&Channels) + превью последних сообщений
 *       по каждому чату (Chat History) — поток getHomepage со слайдов 100–104;</li>
 *   <li>отдаёт статичную HTML-страницу для ручного теста WebSocket-доставки.</li>
 * </ul>
 */
@SpringBootApplication
public class WebAppServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppServiceApplication.class, args);
    }
}
