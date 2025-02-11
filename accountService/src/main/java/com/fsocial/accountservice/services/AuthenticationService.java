package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.IntrospectRequest;
import com.fsocial.accountservice.dto.request.auth.LogoutRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticationAccount(AccountLoginRequest request);
    IntrospectResponse introspectValid(IntrospectRequest request);
    void logout(LogoutRequest token)  throws JOSEException, ParseException;
}