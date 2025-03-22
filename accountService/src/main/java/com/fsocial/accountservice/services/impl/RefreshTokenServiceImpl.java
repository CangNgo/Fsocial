package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.RefreshToken;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RefreshTokenRepository;
import com.fsocial.accountservice.services.JwtService;
import com.fsocial.accountservice.services.RefreshTokenService;
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
import java.time.Duration;
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

        long tokenCount = refreshTokenRepository.countByUsername(account.getUsername());
        int MAX_REFRESH_TOKENS = 5;
        if (tokenCount >= MAX_REFRESH_TOKENS) refreshTokenRepository.deleteOldestTokenByUsername(account.getUsername());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateRefreshToken())
                .username(username)
                .userAgent(currentUserAgent)
                .ipAddress(currentIp)
                .expiryDate(Instant.now().plus(expirationTime, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public RefreshToken validRefreshToken(String token, String currentUserAgent, String currentIp) {
        RefreshToken existedRT = getRefreshToken(token);

        if (existedRT.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(existedRT);
            log.warn("Refresh token {} đã hết hạn.", token);
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        if (!existedRT.getUserAgent().equals(currentUserAgent)) throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (!existedRT.getIpAddress().equals(currentIp)) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return existedRT;
    }

    @Override
    public AuthenticationResponse refreshAccessToken(String refreshToken, String userAgent, String ipAddress) {
        RefreshToken token = validRefreshToken(refreshToken, userAgent, ipAddress);

        if (Duration.between(Instant.now(), token.getExpiryDate()).toHours() < 24) {
            log.info("Gia hạn RefreshToken thành công.");
            token.setExpiryDate(Instant.now().plus(expirationTime, ChronoUnit.DAYS));
            refreshTokenRepository.save(token);
        }

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(token.getUsername()))
                .refreshToken(token.getToken())
                .build();
    }

    @Override
    @Transactional
    public void disableRefreshToken(String refreshToken) {
        var token = getRefreshToken(refreshToken);
        refreshTokenRepository.deleteByToken(token.getToken());
    }

    private RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token không hợp lệ: {}", refreshToken);
                    return new AppException(ErrorCode.INVALID_TOKEN);
                });
    }

    private String generateRefreshToken() {
        try {
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(SecureRandom.getInstanceStrong().generateSeed(32));
        } catch (NoSuchAlgorithmException e) {
            log.error("Xảy ra lỗi trong quá trình khởi tạo Refresh Token. {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
