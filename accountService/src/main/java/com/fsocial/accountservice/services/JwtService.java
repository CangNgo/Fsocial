package com.fsocial.accountservice.services;

public interface JwtService {
    String generateToken(String username);
    boolean verifyToken(String token);
    byte[] getSignerKey();
}
