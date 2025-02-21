package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import com.fsocial.accountservice.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username, String currentUserAgent, String currentIp);
    RefreshToken validRefreshToken(String token, String currentUserAgent, String currentIp);
    AuthenticationResponse refreshAccessToken(String refreshToken, String userAgent, String ipAddress);
    void disableRefreshToken(String refreshToken);
}

