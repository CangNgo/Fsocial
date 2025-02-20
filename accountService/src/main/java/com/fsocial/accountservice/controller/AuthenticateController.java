package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.RefreshTokenRequest;
import com.fsocial.accountservice.dto.request.auth.TokenRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.services.AuthenticationService;
import com.fsocial.accountservice.services.JwtService;
import com.fsocial.accountservice.services.RefreshTokenService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AuthenticateController {

    AuthenticationService authenticationService;
    RefreshTokenService refreshTokenService;
    JwtService jwtService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> verifyRefreshToken(@RequestBody @Valid TokenRequest refreshToken,
                                                              @RequestHeader("User-Agent") String userAgent,
                                                              HttpServletRequest httpRequest) throws ParseException, JOSEException {
        String ipAddress = httpRequest.getRemoteAddr();

        return ApiResponse.<IntrospectResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(
                        authenticationService.introspect(refreshToken, userAgent, ipAddress)
                )
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> handleLogin(@RequestBody @Valid AccountLoginRequest request,
                                                           @RequestHeader("User-Agent") String userAgent,
                                                           HttpServletRequest httpRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .data(
                        authenticationService.login(request, userAgent, httpRequest)
                )
                .dateTime(LocalDateTime.now())
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshAccessToken(@RequestBody @Valid RefreshTokenRequest request,
                                                                  @RequestHeader("User-Agent") String userAgent,
                                                                  HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        return ApiResponse.<AuthenticationResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(
                        refreshTokenService.refreshAccessToken(request.getRefreshToken(), userAgent, ipAddress)
                )
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestBody @Valid TokenRequest refreshToken,
                                    HttpServletRequest httpRequest) {
        refreshTokenService.disableRefreshToken(refreshToken.getToken());
        SecurityContextHolder.clearContext();
        httpRequest.getSession().invalidate();
        return ApiResponse.builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }
}
