package com.javaguru.messaging.users.repository;

import com.javaguru.messaging.users.domain.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {
}
