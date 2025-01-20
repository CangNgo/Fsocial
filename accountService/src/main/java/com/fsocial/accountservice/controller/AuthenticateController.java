package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.IntrospectRequest;
import com.fsocial.accountservice.dto.request.auth.LogoutRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.services.impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AuthenticateController {

    AuthenticationServiceImpl authenticationService;

    @PostMapping("/public/introspect")
    public ApiResponse<IntrospectResponse> introspectValid(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Verify token success.")
                .data(
                        authenticationService.introspectValid(request)
                )
                .build();
    }

    @PostMapping("/public/login")
    public ApiResponse<AuthenticationResponse> handleLogin(@RequestBody @Valid AccountLoginRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Login success.")
                .data(
                        authenticationService.authenticationAccount(request)
                )
                .build();
    }

    @PostMapping("/public/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Logout success.")
                .build();
    }
}
