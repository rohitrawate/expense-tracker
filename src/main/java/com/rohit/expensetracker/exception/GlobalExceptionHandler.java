package com.rohit.expensetracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request  )
    {
        List<ValidationError> validationErrors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(this::toValidationError)
                        .toList();

        ApiErrorResponse response =
                new ApiErrorResponse(
                        false,
                        "ValidationError",
                        Instant.now(),
                        request.getRequestURI(),
                        validationErrors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateEmail(
            EmailAlreadyExistsException ex,
            HttpServletRequest request ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        false,
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI(),
                        List.of()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRoleNotFound(
            RoleNotFoundException ex,
            HttpServletRequest request )
    {
        ApiErrorResponse response =
                new ApiErrorResponse(
                        false,
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI(),
                        List.of()
                );

        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


    private ValidationError toValidationError(FieldError fieldError) {

        return new ValidationError(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }
}
