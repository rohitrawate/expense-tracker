package com.rohit.expensetracker.controller.health;

import com.rohit.expensetracker.dto.HealthResponse;
import com.rohit.expensetracker.service.health.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/api/v1/health")
    public HealthResponse health() {
        return healthService.getStatus();
    }
}