package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.IntrospectRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.exception.StatusCode;
import com.fsocial.accountservice.repository.AccountRepository;
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
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticationAccount(AccountLoginRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(StatusCode.NOT_EXIST)
        );

        boolean valid = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!valid)
            throw new AppException(StatusCode.UNAUTHENTICATED);

        String token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public IntrospectResponse introspectValid(IntrospectRequest request) {
        String token = request.getToken();

        try {
            JWSVerifier verifier = new MACVerifier(getSignerKey());
            SignedJWT signedJWT = SignedJWT.parse(token);

            return IntrospectResponse.builder()
                    .valid(signedJWT.verify(verifier))
                    .build();

        } catch (JOSEException | ParseException e) {
            throw new AppException(StatusCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    protected String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("FSOCIAL - FCODER")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(getSignerKey()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

//    private String buildScope(User user) {
//        return CollectionUtils.isEmpty(user.getRoles()) ? "" : String.join(" ", user.getRoles());
//    }

    private byte[] getSignerKey() {
        if (SIGNER_KEY == null || SIGNER_KEY.isEmpty()) throw new AppException(StatusCode.UNCATEGORIZED_EXCEPTION);
        return SIGNER_KEY.getBytes();
    }
}
