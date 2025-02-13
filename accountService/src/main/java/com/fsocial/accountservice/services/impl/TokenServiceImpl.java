package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.InvalidToken;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.repository.InvalidTokenRepository;
import com.fsocial.accountservice.services.TokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenServiceImpl implements TokenService {

    InvalidTokenRepository invalidTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @Override
    public String generateToken(Account account) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(account.getUsername())
                    .issuer("FSOCIAL - FCODER")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim(
                            "scope",
                            Optional.ofNullable(account.getRole()).map(Role::getName).orElse(" "))
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS256),
                    new Payload(claimsSet.toJSONObject())
            );
            jwsObject.sign(new MACSigner(getSignerKey()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không tạo được token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(getSignerKey());

        if (!signedJWT.verify(verifier) || isTokenExpired(signedJWT) || isTokenInvalidated(signedJWT))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    @Override
    public void invalidateToken(String token) {
        try {
            SignedJWT signedJWT = verifyToken(token);
            invalidTokenRepository.save(InvalidToken.builder()
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .build());
        } catch (JOSEException | ParseException e) {
            log.error("Không thể làm mất hiệu lực mã token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        return signedJWT.getJWTClaimsSet().getExpirationTime().toInstant().isBefore(Instant.now());
    }

    private boolean isTokenInvalidated(SignedJWT signedJWT) throws ParseException {
        return invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
    }

    public byte[] getSignerKey() {
        if (signerKey == null || signerKey.isEmpty()) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        return signerKey.getBytes();
    }
}
