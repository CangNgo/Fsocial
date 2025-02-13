package com.fsocial.accountservice.services;

public interface OtpService {
    void sendOtp(String email, String keyPrefix);
    void validateOtp(String email, String otp, String keyPrefix);
    void deleteOtp(String email);
    void validEmailBeforePersist(String email);
}
