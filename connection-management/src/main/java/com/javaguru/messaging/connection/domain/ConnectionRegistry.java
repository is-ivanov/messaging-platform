package com.javaguru.messaging.connection.domain;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Key/Value реестр соединений: {@code userId → serverUrl} (слайды 115–119).
 *
 * <p>Заглушка Key/Value DB — обычный {@link ConcurrentHashMap}. В проде это Redis.
 * Почему KV/in-memory, а не SQL: данные эфемерные и высокочастотные, доступ строго по ключу,
 * а после рестарта соединения всё равно невалидны — персистентность не нужна.
 */
@Component
public class ConnectionRegistry {

    private final Map<String, String> userToServer = new ConcurrentHashMap<>();

    /** Зафиксировать, что {@code userId} подключён к Messaging Server по адресу {@code serverUrl}. */
    public void register(String userId, String serverUrl) {
        userToServer.put(userId, serverUrl);
    }

    /** Убрать соединение (на disconnect). */
    public void remove(String userId) {
        userToServer.remove(userId);
    }

    /** Адрес сервера, на котором висит соединение пользователя, либо empty, если он офлайн. */
    public Optional<String> serverOf(String userId) {
        return Optional.ofNullable(userToServer.get(userId));
    }

    /** Снимок реестра (для отладки/диагностики). */
    public Map<String, String> snapshot() {
        return Map.copyOf(userToServer);
    }
}
