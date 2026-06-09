package com.javaguru.messaging.connection.web;

import com.javaguru.messaging.connection.service.RoutingService;
import com.javaguru.messaging.connection.web.dto.RouteRequest;
import com.javaguru.messaging.connection.web.dto.RouteResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Оркестрация доставки (зовёт тонкий Messaging Server при входящем сообщении).
 * Вся «убранная из messaging service» логика (слайд 133) живёт за этим эндпоинтом.
 */
@RestController
@RequestMapping("/internal/messages")
public class RouteController {

    private final RoutingService routing;

    public RouteController(RoutingService routing) {
        this.routing = routing;
    }

    @PostMapping("/route")
    public RouteResponse route(@Valid @RequestBody RouteRequest request) {
        return routing.route(request);
    }
}
