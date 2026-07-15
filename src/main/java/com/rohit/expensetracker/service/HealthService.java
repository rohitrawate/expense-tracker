package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.HealthResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthResponse getStatus() {
        return new HealthResponse(
                "UP",
                "Expense Tracker",
                "0.0.1-SNAPSHOT"
        );
    }
}