package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.HealthResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class HealthService {

    private final Clock clock;

    public HealthService(Clock clock) {
        this.clock = clock;
        System.out.println("1. Constructor Called");
    }

    @PostConstruct
    public void intialize() {
        System.out.println("2. PostConstruct Executed");
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("3. Bean Destroyed");
    }

    public HealthResponse getStatus() {
        return new HealthResponse(
                "UP",
                "Expense Tracker",
                "0.0.1-SNAPSHOT",
                Instant.now(clock)
        );
    }
}