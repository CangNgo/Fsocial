package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.IntrospectRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.services.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    AuthenticationServiceImpl authenticationService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspectValid(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Verify token success.")
                .data(
                        authenticationService.introspectValid(request)
                )
                .build();
    }

    @PostMapping
    public ApiResponse<AuthenticationResponse> handleLogin(@RequestBody @Valid AccountLoginRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .statusCode(StatusCode.OK.getCode())
                .message("Login success.")
                .data(
                        authenticationService.authenticationAccount(request)
                )
                .build();
    }
}
