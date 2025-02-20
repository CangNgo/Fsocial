package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.request.account.EmailRequest;
import com.fsocial.accountservice.dto.request.account.OtpRequest;

public interface OtpService {
    void sendOtp(String email, String keyPrefix);
    void validateOtp(String email, String otp, String keyPrefix);
    void deleteOtp(String email, String keyPrefix);
    void validEmailBeforePersist(String email);
    void sortTypeForSendOtp(EmailRequest request);
    void sortTypeForVerifyOtp(OtpRequest request);
}
