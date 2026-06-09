package com.javaguru.messaging.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Аутентификация на шлюзе: единожды валидирует токен через Users и проставляет вниз {@code X-User-Id}.
 * Бэкенды доверяют только этому заголовку (а не клиентскому) — анти-спуфинг, закрывает §9.1.
 *
 * <p>Публичные пути (signup/login, статика, WS-handshake) пропускаются без токена; на них клиентский
 * {@code X-User-Id} срезается. WS валидирует токен сам на handshake, поэтому здесь /ws не трогаем.
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final WebClient users;

    public AuthGlobalFilter(@Value("${services.users-url}") String usersUrl) {
        // WebClient.create(baseUrl) — без бина WebClient.Builder (в Boot 4.0 он не авто-конфигурится)
        this.users = WebClient.create(usersUrl);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isPublic(path)) {
            // на публичных путях клиентский X-User-Id не доверяем — срезаем
            return chain.filter(exchange.mutate()
                    .request(r -> r.headers(h -> h.remove("X-User-Id")))
                    .build());
        }

        String token = extractToken(request);
        if (token == null) {
            return reject(exchange, "нет токена (Authorization: Bearer ... или ?token=)");
        }

        return users.post()
                .uri("/internal/auth/validate")
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(ValidateResponse.class)
                .flatMap(v -> chain.filter(exchange.mutate()
                        .request(r -> r.headers(h -> h.set("X-User-Id", v.userId())))
                        .build()))
                .onErrorResume(e -> reject(exchange, "невалидный или истёкший токен"));
    }

    private boolean isPublic(String path) {
        // /ws НЕ публичный: токен в ?token= валидируем здесь же (чистый 401 на handshake);
        // messaging-server всё равно перепроверяет токен сам (defense in depth).
        return path.equals("/")
                || path.equals("/index.html")
                || path.startsWith("/static/")
                || path.equals("/favicon.ico")
                || path.equals("/api/users/signup")
                || path.equals("/api/users/login");
    }

    private String extractToken(ServerHttpRequest request) {
        String auth = request.getHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        String query = request.getQueryParams().getFirst("token");
        return (query != null && !query.isBlank()) ? query : null;
    }

    private Mono<Void> reject(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"code\":\"UNAUTHORIZED\",\"message\":\"" + message + "\"}")
                .getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body)));
    }

    @Override
    public int getOrder() {
        // раньше штатных фильтров маршрутизации, чтобы X-User-Id успел проставиться
        return -1;
    }

    /** Ответ Users {@code /internal/auth/validate}. */
    public record ValidateResponse(String userId) {
    }
}
