package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.TokenRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse login(AccountLoginRequest request, String userAgent, HttpServletRequest httpRequest);
    IntrospectResponse introspect(TokenRequest request, String userAgent, String ipAddress) throws ParseException, JOSEException;
}