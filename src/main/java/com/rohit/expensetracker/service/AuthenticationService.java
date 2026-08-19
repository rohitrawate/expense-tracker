package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.auth.LoginRequest;
import com.rohit.expensetracker.dto.auth.RegisterRequest;
import com.rohit.expensetracker.dto.auth.RegisterResponse;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest request);

    Authentication authenticate(LoginRequest request);
}