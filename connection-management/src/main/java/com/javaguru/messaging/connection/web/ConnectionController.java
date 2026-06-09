package com.javaguru.messaging.connection.web;

import com.javaguru.messaging.connection.client.UsersClient;
import com.javaguru.messaging.connection.domain.ConnectionRegistry;
import com.javaguru.messaging.connection.web.dto.RegisterConnectionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Реестр соединений (зовёт тонкий Messaging Server на connect/disconnect).
 * CM — владелец presence: заодно best-effort флипает статус ONLINE/OFFLINE в Users.
 */
@RestController
@RequestMapping("/internal/connections")
public class ConnectionController {

    private final ConnectionRegistry registry;
    private final UsersClient users;

    public ConnectionController(ConnectionRegistry registry, UsersClient users) {
        this.registry = registry;
        this.users = users;
    }

    /** Пользователь подключился к Messaging Server {@code serverUrl}. */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@PathVariable String userId, @Valid @RequestBody RegisterConnectionRequest request) {
        registry.register(userId, request.serverUrl());
        users.markStatus(userId, "ONLINE");
    }

    /** Пользователь отключился. */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deregister(@PathVariable String userId) {
        registry.remove(userId);
        users.markStatus(userId, "OFFLINE");
    }

    /** Снимок реестра {@code userId → serverUrl} (для отладки/диагностики). */
    @GetMapping
    public Map<String, String> all() {
        return registry.snapshot();
    }
}
