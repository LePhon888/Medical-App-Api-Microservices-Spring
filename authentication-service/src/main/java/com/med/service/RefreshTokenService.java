package com.med.service;

import com.med.model.RefreshToken;
import com.med.repository.RefreshTokenRepository;
import com.med.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String email) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusMonths(3);
        Timestamp expiryDate = Timestamp.from(expiry.atZone(ZoneId.systemDefault()).toInstant());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.getUserByEmail(email))
                .token(UUID.randomUUID().toString())
                .expiryDate(expiryDate)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        if (token.getExpiryDate().before(currentTimestamp)) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

}