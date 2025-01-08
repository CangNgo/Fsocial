package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.IntrospectRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticationAccount(AccountLoginRequest request);
    IntrospectResponse introspectValid(IntrospectRequest request);
}