package com.rohit.expensetracker.dto.auth;

import java.util.Set;
import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String email,
        Set<String> roles
    ) {  }