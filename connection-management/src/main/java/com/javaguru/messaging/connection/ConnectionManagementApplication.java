package com.javaguru.messaging.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Connection Management (порт 8090) — NFR-слой, слайды 114–134.
 *
 * <p>Две роли:
 * <ol>
 *   <li><b>Key/Value реестр соединений</b> {@code userId → serverUrl} (на каком Messaging Server
 *       висит сокет пользователя) + presence;</li>
 *   <li><b>оркестрация доставки</b>: store → получатели → маршрутизация по реестру →
 *       fan-out онлайн-получателям (слайд 133: «убрали логику из messaging service»).</li>
 * </ol>
 */
@SpringBootApplication
public class ConnectionManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectionManagementApplication.class, args);
    }
}
