package com.rohit.expensetracker.service;

import com.rohit.expensetracker.dto.auth.LoginRequest;
import com.rohit.expensetracker.dto.auth.RegisterRequest;
import com.rohit.expensetracker.dto.auth.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest request);

    void authenticate(LoginRequest request);
}