package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.auth.IntrospectResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.exception.AppCheckedException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponse login(AccountLoginRequest request, String userAgent, HttpServletRequest httpRequest)
            throws AppCheckedException;

    // Method mới cho Google Login
    AuthenticationResponse loginGoogle(Account account, String userAgent, HttpServletRequest httpRequest);

    String getTokenFromRequest();

    IntrospectResponse introspect(String token);
}