package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.IntrospectRequest;
import com.fsocial.accountservice.dto.request.auth.LogoutRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.enums.StatusCode;
import com.fsocial.accountservice.services.impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AuthenticateController {

    AuthenticationServiceImpl authenticationService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspectValid(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .data(
                        authenticationService.introspectValid(request)
                )
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> handleLogin(@RequestBody @Valid AccountLoginRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .data(
                        authenticationService.authenticationAccount(request)
                )
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .build();
    }
}
