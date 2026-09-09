package com.rohit.expensetracker.service;

import com.rohit.expensetracker.entity.RefreshToken;
import com.rohit.expensetracker.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(User user);

}
