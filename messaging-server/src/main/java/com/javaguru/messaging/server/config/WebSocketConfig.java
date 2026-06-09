package com.javaguru.messaging.server.config;

import com.javaguru.messaging.server.ws.AuthHandshakeInterceptor;
import com.javaguru.messaging.server.ws.MessagingWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/** Регистрирует raw-WebSocket эндпоинт {@code /ws} с auth-перехватчиком handshake. */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessagingWebSocketHandler handler;
    private final AuthHandshakeInterceptor authInterceptor;

    public WebSocketConfig(MessagingWebSocketHandler handler, AuthHandshakeInterceptor authInterceptor) {
        this.handler = handler;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws")
                .addInterceptors(authInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
