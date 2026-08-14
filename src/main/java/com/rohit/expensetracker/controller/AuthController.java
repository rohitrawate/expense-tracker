package com.rohit.expensetracker.controller;

import com.rohit.expensetracker.common.ApiResponse;
import com.rohit.expensetracker.dto.auth.LoginRequest;
import com.rohit.expensetracker.dto.auth.RegisterRequest;
import com.rohit.expensetracker.dto.auth.RegisterResponse;
import com.rohit.expensetracker.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request  )
    {
        System.out.println(">>> Register endpoint invoked");
        RegisterResponse response =  authenticationService.register(request);

        ApiResponse<RegisterResponse> apiResponse =
                ApiResponse.<RegisterResponse>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Login endpoint reached")
                        .data(null)
                        .build()
        );
    }

}
