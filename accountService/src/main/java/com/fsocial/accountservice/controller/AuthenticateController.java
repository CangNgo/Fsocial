package com.fsocial.accountservice.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.RefreshTokenRequest;
import com.fsocial.accountservice.dto.request.auth.TokenRequest;
import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.auth.IntrospectResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.exception.AppCheckedException;
import com.fsocial.accountservice.services.AuthenticationService;
import com.fsocial.accountservice.services.JwtService;
import com.fsocial.accountservice.services.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Slf4j
public class AuthenticateController {

    AuthenticationService authenticationService;
    RefreshTokenService refreshTokenService;
    JwtService jwtService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid TokenRequest token) {
        return buildResponse(authenticationService.introspect(token.getToken()));
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> handleLogin(@RequestBody @Valid AccountLoginRequest request,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletRequest httpRequest) throws AppCheckedException {
        return buildResponse(authenticationService.login(request, userAgent, httpRequest));
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshAccessToken(@RequestBody @Valid RefreshTokenRequest request,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        return buildResponse(refreshTokenService.refreshAccessToken(request.getRefreshToken(), userAgent, ipAddress));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestBody @Valid TokenRequest refreshToken,
            HttpServletRequest httpRequest) {
        refreshTokenService.disableRefreshToken(refreshToken.getToken());
        SecurityContextHolder.clearContext();
        httpRequest.getSession().invalidate();
        return buildResponse(null);
    }

    private <T> ApiResponse<T> buildResponse(T data) {
        return ApiResponse.<T>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }
}
