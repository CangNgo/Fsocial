package com.fsocial.accountservice.services;

import com.fsocial.accountservice.entity.Account;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;

public interface TokenService {
    String generateToken(Account account);
    SignedJWT verifyToken(String token) throws JOSEException, ParseException;
    void invalidateToken(String token);
}
