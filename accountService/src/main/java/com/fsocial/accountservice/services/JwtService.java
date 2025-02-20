package com.fsocial.accountservice.services;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JwtService {
    String generateToken(String username);
    void verifyAccessToken(String token) throws JOSEException, ParseException;
    void disableToken(String token);
}
