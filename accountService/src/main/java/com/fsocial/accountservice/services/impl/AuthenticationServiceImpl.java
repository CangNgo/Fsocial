package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.IntrospectRequest;
import com.fsocial.accountservice.dto.request.LogoutRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.InvalidToken;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.InvalidTokenRepository;
import com.fsocial.accountservice.services.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    InvalidTokenRepository invalidTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticationAccount(AccountLoginRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(StatusCode.NOT_EXIST));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new AppException(StatusCode.UNAUTHENTICATED);
        }

        String token = generateToken(account);
        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public IntrospectResponse introspectValid(IntrospectRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.getToken());
            JWSVerifier verifier = new MACVerifier(getSignerKey());

            boolean isValid = signedJWT.verify(verifier);
            return IntrospectResponse.builder()
                    .valid(isValid)
                    .build();
        } catch (JOSEException | ParseException e) {
            throw new AppException(StatusCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken());
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            InvalidToken invalidToken = InvalidToken.builder()
                    .id(claimsSet.getJWTID())
                    .expiryTime(claimsSet.getExpirationTime())
                    .build();

            invalidTokenRepository.save(invalidToken);
        } catch (JOSEException | ParseException e) {
            throw new AppException(StatusCode.UNAUTHENTICATED);
        }
    }

    private String generateToken(Account account) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(account.getUsername())
                    .issuer("FSOCIAL - FCODER")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(account))
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS512),
                    new Payload(claimsSet.toJSONObject())
            );

            jwsObject.sign(new MACSigner(getSignerKey()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(StatusCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(getSignerKey());

        if (!signedJWT.verify(verifier) || isTokenExpired(signedJWT) || isTokenInvalidated(signedJWT)) {
            throw new AppException(StatusCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        return signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
    }

    private boolean isTokenInvalidated(SignedJWT signedJWT) throws ParseException {
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        return invalidTokenRepository.existsById(jwtId);
    }

    private String buildScope(Account account) {
        return Optional.ofNullable(account.getRole())
                .map(Role::getName)
                .orElse(" ");
    }

    private byte[] getSignerKey() {
        if (SIGNER_KEY == null || SIGNER_KEY.isEmpty()) throw new AppException(StatusCode.UNCATEGORIZED_EXCEPTION);
        return SIGNER_KEY.getBytes();
    }
}
