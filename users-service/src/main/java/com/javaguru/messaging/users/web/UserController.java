package com.javaguru.messaging.users.web;

import com.javaguru.messaging.users.service.UserService;
import com.javaguru.messaging.users.web.dto.AuthResponse;
import com.javaguru.messaging.users.web.dto.LoginRequest;
import com.javaguru.messaging.users.web.dto.SignupRequest;
import com.javaguru.messaging.users.web.dto.UserSummary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Внешний API Users Service (см. слайды 29, 57).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@Valid @RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/search")
    public UserSummary search(@RequestParam String username) {
        return UserSummary.from(userService.searchByUsername(username));
    }

    @GetMapping("/{id}")
    public UserSummary getById(@PathVariable String id) {
        return UserSummary.from(userService.getById(id));
    }
}
