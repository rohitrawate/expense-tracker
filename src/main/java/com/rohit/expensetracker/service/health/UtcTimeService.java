package com.rohit.expensetracker.service.health;

import java.time.Clock;
import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
//@Primary
public class UtcTimeService implements TimeService {

    private final Clock clock;

    public UtcTimeService(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String currentTime() {
        return Instant.now(clock).toString();
    }

}