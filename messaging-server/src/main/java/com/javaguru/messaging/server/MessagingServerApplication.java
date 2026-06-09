package com.javaguru.messaging.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Messaging Server — тонкий WebSocket-слой (NFR, слайды 108–113).
 *
 * <p>Запускается в нескольких экземплярах (8084, 8085, …). Держит WebSocket-соединения СВОИХ
 * клиентов в локальной {@code Map<userId, session>} и больше ничего: бизнес-логика доставки
 * вынесена в Connection Management (слайд 133 «убрали логику из messaging service»).
 *
 * <p>Роль инстанса:
 * <ul>
 *   <li>handshake: валидирует токен через Users → кладёт userId в сессию;</li>
 *   <li>connect/disconnect: регистрирует/снимает соединение в Connection Management;</li>
 *   <li>входящее сообщение: пересылает в Connection Management (senderId — из auth-контекста);</li>
 *   <li>{@code POST /internal/deliver}: пушит MESSAGE локально подключённому получателю.</li>
 * </ul>
 */
@SpringBootApplication
public class MessagingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagingServerApplication.class, args);
    }
}
