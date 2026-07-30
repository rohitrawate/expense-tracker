package com.rohit.expensetracker.service;

import com.rohit.expensetracker.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);
}
