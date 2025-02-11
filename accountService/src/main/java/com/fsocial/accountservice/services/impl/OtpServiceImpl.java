package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.StatusCode;
import com.fsocial.accountservice.services.OtpService;
import com.fsocial.accountservice.util.MailUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {

    RedisTemplate<String, String> redisTemplate;
    MailUtils mailUtils;

    @Override
    public void sendOtp(String email, String keyPrefix) {
        String otp = generateOtp();
        String redisKey = keyPrefix + email;
        redisTemplate.opsForValue().set(redisKey, otp, 5, TimeUnit.MINUTES);
        mailUtils.sendOtp(email, otp);
        log.info("Sent OTP to {}", email);
    }

    @Override
    public void validateOtp(String email, String otp, String keyPrefix) {
        String redisKey = keyPrefix + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new AppException(StatusCode.OTP_INVALID);
        }
        redisTemplate.delete(redisKey);
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}
