package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Permission;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RefreshTokenRepository;
import com.fsocial.accountservice.repository.RoleRepository;
import com.fsocial.accountservice.services.JwtService;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtServiceImpl implements JwtService {

    RoleRepository roleRepository;
    AccountRepository accountRepository;
    RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @NonFinal
    @Value("${jwt.duration}")
    long durationTime;

    @Override
    public String generateToken(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        JWTClaimsSet claimsSet = buildClaimsSet(account);

        return signToken(claimsSet);
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(getSignerKey());
            SignedJWT signedJWT = SignedJWT.parse(token);

            return signedJWT.verify(verifier) && !isTokenExpired(signedJWT);
        } catch (JOSEException | ParseException e) {
            log.error("Có lỗi trong quá trình phân tích Token.");
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public byte[] getSignerKey() {
        if (signerKey == null || signerKey.isEmpty()) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        return signerKey.getBytes();
    }

    private JWTClaimsSet buildClaimsSet(Account account) {
        String issuerValue = "FSOCIAL - FCODER";

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer(issuerValue)
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(durationTime, ChronoUnit.SECONDS).toEpochMilli())
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", account.getRole() != null ? account.getRole().getName() : "");

        return claimsBuilder.build();
    }

    private String signToken(JWTClaimsSet claimsSet) {
        try {
            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS256),
                    new Payload(claimsSet.toJSONObject())
            );

            byte[] signerKey = getSignerKey();
            if (signerKey.length < 32) {
                throw new AppException(ErrorCode.WEAK_SECRET_KEY);
            }

            jwsObject.sign(new MACSigner(signerKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không tạo được token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        return signedJWT.getJWTClaimsSet().getExpirationTime().toInstant().isBefore(Instant.now());
    }
}
