package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.HealthResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class HealthService {

    private final TimeService timeService;
    private final ApplicationInfoService appInfo;

    public HealthService(@Qualifier("localTimeService") TimeService timeService,
                         ApplicationInfoService appInfo) {
        this.timeService = timeService;
        this.appInfo = appInfo;
    }

    public HealthResponse getStatus() {
        return new HealthResponse(
                "UP",
                appInfo.applicationName(),
                appInfo.version(),
                timeService.currentTime()
        );
    }
}