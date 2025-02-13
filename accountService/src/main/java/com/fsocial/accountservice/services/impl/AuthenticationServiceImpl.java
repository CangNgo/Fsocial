package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.TokenRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.httpclient.ProfileClient;
import com.fsocial.accountservice.services.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    TokenServiceImpl tokenService;

    @Override
    public AuthenticationResponse login(AccountLoginRequest request) {
        Account account = accountRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (account.getPassword() == null || !passwordEncoder.matches(request.getPassword(), account.getPassword()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return AuthenticationResponse.builder()
                .token(tokenService.generateToken(account))
                .build();
    }

    @Override
    public IntrospectResponse introspectValid(TokenRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.getToken());
            return IntrospectResponse.builder()
                    .valid(signedJWT.verify(new MACVerifier(tokenService.getSignerKey())))
                    .build();
        } catch (JOSEException | ParseException e) {
            log.error("Xác minh token không thành công: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void logout(TokenRequest request) {
        if (request == null || request.getToken() == null) throw new AppException(ErrorCode.OTP_INVALID);
        tokenService.invalidateToken(request.getToken());
    }

}
