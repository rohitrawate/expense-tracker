package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.HealthResponse;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class HealthService {

    private final Clock clock;

    public HealthService(Clock clock) {
        this.clock = clock;
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