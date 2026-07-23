package com.rohit.expensetracker.dto;

public record HealthResponse(
        String status,
        String application,
        String version,
        String now
) {}