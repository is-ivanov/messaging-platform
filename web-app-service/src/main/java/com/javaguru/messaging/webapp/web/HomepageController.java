package com.javaguru.messaging.webapp.web;

import com.javaguru.messaging.webapp.service.HomepageService;
import com.javaguru.messaging.webapp.web.dto.HomepageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Homepage пользователя (слайд 101). Личность — из заголовка {@code X-User-Id}
 * (его проставляет API Gateway после валидации токена; см. §9.1 плана).
 */
@RestController
public class HomepageController {

    private final HomepageService service;

    public HomepageController(HomepageService service) {
        this.service = service;
    }

    @GetMapping("/api/homepage")
    public HomepageResponse homepage(@RequestHeader("X-User-Id") String userId,
                                     @RequestParam(defaultValue = "20") int limit) {
        return service.build(userId, limit);
    }
}
