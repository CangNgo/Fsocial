package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.IntrospectRequest;
import com.fsocial.accountservice.dto.request.auth.LogoutRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.dto.response.ProfileResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.InvalidToken;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.StatusCode;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.httpclient.ProfileClient;
import com.fsocial.accountservice.services.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    TokenServiceImpl tokenService;

    @Override
    public AuthenticationResponse authenticationAccount(AccountLoginRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername())
                .or(() -> accountRepository.findByEmail(request.getUsername()))
                .orElseThrow(() -> new AppException(StatusCode.NOT_EXIST));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword()))
            throw new AppException(StatusCode.UNAUTHENTICATED);

        ProfileResponse profileResponse = profileClient.getProfileByUserId(account.getId());

        return AuthenticationResponse.builder()
                .token(tokenService.generateToken(account))
                .build();
    }

    @Override
    public IntrospectResponse introspectValid(IntrospectRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.getToken());
            return IntrospectResponse.builder()
                    .valid(signedJWT.verify(new MACVerifier(tokenService.getSignerKey())))
                    .build();
        } catch (JOSEException | ParseException e) {
            throw new AppException(StatusCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void logout(LogoutRequest request) {
        tokenService.invalidateToken(request.getToken());
    }

}
