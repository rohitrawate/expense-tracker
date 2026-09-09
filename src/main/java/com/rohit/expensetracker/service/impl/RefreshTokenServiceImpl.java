package com.rohit.expensetracker.service.impl;

import com.rohit.expensetracker.entity.RefreshToken;
import com.rohit.expensetracker.entity.User;
import com.rohit.expensetracker.repository.RefreshTokenRepository;
import com.rohit.expensetracker.service.RefreshTokenService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private static final int TOKEN_BYTES = 64;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public RefreshToken createRefreshToken(User user) {

        byte[] randomBytes = new byte[TOKEN_BYTES];

        secureRandom.nextBytes(randomBytes);

        String token = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);

        RefreshToken refreshToken = RefreshToken.builder()
                                    .token(token)
                                    .expiryDate(
                                            LocalDateTime.now().plusDays(30)
                                    )
                                    .user(user)
                                    .revoked(false)
                                    .build();

        return refreshTokenRepository.save(refreshToken);
    }

}
