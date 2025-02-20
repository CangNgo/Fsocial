package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username, String currentUserAgent, String currentIp);
    RefreshToken validateRefreshToken(String token, String currentUserAgent, String currentIp);
    AuthenticationResponse refreshAccessToken(String refreshToken, String userAgent, HttpServletRequest httpRequest);
}
