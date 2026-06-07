package com.javaguru.messaging.users.web;

import com.javaguru.messaging.users.service.TokenService;
import com.javaguru.messaging.users.service.UserService;
import com.javaguru.messaging.users.web.dto.BatchRequest;
import com.javaguru.messaging.users.web.dto.StatusUpdateRequest;
import com.javaguru.messaging.users.web.dto.UserSummary;
import com.javaguru.messaging.users.web.dto.ValidateRequest;
import com.javaguru.messaging.users.web.dto.ValidateResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Внутренний API для других сервисов (не для конечного клиента):
 * - валидация токена;
 * - смена статуса online/offline (зовёт Messaging Service);
 * - batch-выдача профилей (для сборки homepage).
 *
 * В реальной системе этот префикс был бы скрыт за API Gateway / внутренней сетью.
 */
@RestController
@RequestMapping("/internal")
public class InternalController {

    private final UserService userService;
    private final TokenService tokenService;

    public InternalController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/validate")
    public ValidateResponse validate(@Valid @RequestBody ValidateRequest request) {
        return new ValidateResponse(tokenService.validate(request.token()));
    }

    @PostMapping("/users/{id}/status")
    public void updateStatus(@PathVariable String id, @Valid @RequestBody StatusUpdateRequest request) {
        userService.updateStatus(id, request.status());
    }

    @PostMapping("/users/batch")
    public List<UserSummary> batch(@Valid @RequestBody BatchRequest request) {
        return userService.getByIds(request.ids()).stream()
                .map(UserSummary::from)
                .toList();
    }
}
