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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<IntrospectResponse> introspectValid(@RequestBody @Valid TokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .data(
                        authenticationService.introspectValid(request)
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

        return ApiResponse.<AuthenticationResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(
                        refreshTokenService.refreshAccessToken(request.getRefreshToken(), userAgent, httpRequest)
                )
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid TokenRequest refreshToken) throws ParseException, JOSEException {
        authenticationService.logout(refreshToken);
        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
    }
}
