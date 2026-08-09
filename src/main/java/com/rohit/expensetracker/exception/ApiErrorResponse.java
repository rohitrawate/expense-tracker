package com.rohit.expensetracker.exception;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        boolean success,
        String message,
        Instant timestamp,
        String path,
        List<ValidationError> errors
) {
}
