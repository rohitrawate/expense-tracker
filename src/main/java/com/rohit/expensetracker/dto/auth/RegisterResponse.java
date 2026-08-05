package com.rohit.expensetracker.dto.auth;

import java.util.UUID;

public record RegisterResponse(

        UUID uuid,
        String firstName,
        String lastName,
        String email
) { }
