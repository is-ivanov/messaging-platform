package com.javaguru.messaging.users.service;

import com.javaguru.messaging.users.domain.AuthToken;
import com.javaguru.messaging.users.domain.User;
import com.javaguru.messaging.users.domain.UserStatus;
import com.javaguru.messaging.users.error.InvalidCredentialsException;
import com.javaguru.messaging.users.error.UserNotFoundException;
import com.javaguru.messaging.users.error.UsernameAlreadyExistsException;
import com.javaguru.messaging.users.repository.UserRepository;
import com.javaguru.messaging.users.web.dto.AuthResponse;
import com.javaguru.messaging.users.web.dto.LoginRequest;
import com.javaguru.messaging.users.web.dto.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository users;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse signup(SignupRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new UsernameAlreadyExistsException(req.username());
        }
        User user = new User(
                UUID.randomUUID().toString(),
                req.username(),
                req.firstName(),
                req.lastName(),
                passwordEncoder.encode(req.password()),
                req.profileImage(),
                UserStatus.OFFLINE,
                Instant.now()
        );
        users.save(user);
        AuthToken token = tokenService.issue(user.getId());
        return new AuthResponse(token.getToken(), user.getId());
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        User user = users.findByUsername(req.username())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        AuthToken token = tokenService.issue(user.getId());
        return new AuthResponse(token.getToken(), user.getId());
    }

    @Transactional(readOnly = true)
    public User searchByUsername(String username) {
        return users.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(readOnly = true)
    public User getById(String id) {
        return users.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<User> getByIds(Collection<String> ids) {
        return users.findByIdIn(ids);
    }

    @Transactional
    public void updateStatus(String id, UserStatus status) {
        User user = getById(id);
        user.setStatus(status);
        // dirty checking сохранит изменение в рамках транзакции
    }
}
