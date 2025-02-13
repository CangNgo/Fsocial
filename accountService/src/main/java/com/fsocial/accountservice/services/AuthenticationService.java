package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.TokenRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse login(AccountLoginRequest request);
    IntrospectResponse introspectValid(TokenRequest request);
    void logout(TokenRequest token)  throws JOSEException, ParseException;
}