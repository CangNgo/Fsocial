package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.RefreshToken;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RefreshTokenRepository;
import com.fsocial.accountservice.services.JwtService;
import com.fsocial.accountservice.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;
    AccountRepository accountRepository;
    JwtService jwtService;

    @NonFinal
    @Value("${jwt.expired-time}")
    long expirationTime;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String username, String currentUserAgent, String currentIp) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        long tokenCount = refreshTokenRepository.countByUsername(username);
        int MAX_REFRESH_TOKENS = 5;
        if (tokenCount >= MAX_REFRESH_TOKENS) refreshTokenRepository.deleteOldestTokenByUsername(username);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateSecureToken())
                .username(username)
                .userAgent(currentUserAgent)
                .ipAddress(currentIp)
                .expiryDate(Instant.now().plus(expirationTime, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateRefreshToken(String token, String currentUserAgent, String currentIp) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Refresh token không hợp lệ: {}", token);
                    return new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
                });

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            log.warn("Refresh token {} đã hết hạn.", token);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        if (!refreshToken.getUserAgent().equals(currentUserAgent) || !refreshToken.getIpAddress().equals(currentIp))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return refreshToken;
    }

    @Override
    public AuthenticationResponse refreshAccessToken(String refreshToken, String userAgent, HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        RefreshToken token = validateRefreshToken(refreshToken, userAgent, ipAddress);

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(token.getUsername()))
                .build();
    }

    private String generateSecureToken() {
        try {
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(SecureRandom.getInstanceStrong().generateSeed(32));
        } catch (NoSuchAlgorithmException e) {
            log.error("Xảy ra lỗi trong quá trình khởi tạo Refresh Token. {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
