package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.HealthResponse;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class HealthService {

    private final Clock clock;
    private final ApplicationInfoService appInfo;

    public HealthService(Clock clock, ApplicationInfoService appInfo) {
        this.clock = clock;
        this.appInfo = appInfo;
    }

    public HealthResponse getStatus() {
        return new HealthResponse(
                "UP",
                appInfo.applicationName(),
                appInfo.version(),
                Instant.now(clock)
        );
    }
}