package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.EmailRequest;
import com.fsocial.accountservice.dto.request.account.OtpRequest;
import com.fsocial.accountservice.enums.RedisKeyType;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.services.OtpService;
import com.fsocial.accountservice.util.MailUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @NonFinal
    @Value("${otp.expiration.send}")
    long durationSend;

    @NonFinal
    @Value("${otp.expiration.verify}")
    long durationVerify;

    @Override
    public void sendOtp(String email, String keyPrefix) {
        String otp = generateOtp();

        if (!keyPrefix.equals(RedisKeyType.REGISTER.getType()) && !keyPrefix.equals(RedisKeyType.RESET.getType()))
            return;

        String redisKey = keyPrefix + email;
        redisTemplate.opsForValue().set(redisKey, otp, durationSend, TimeUnit.MINUTES);
        mailUtils.sendOtp(email, otp);
        log.info("OTP: {}", otp);
    }

    @Override
    public void validateOtp(String email, String otp, String keyPrefix) {
        String redisKey = keyPrefix + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);
        if (storedOtp == null || !storedOtp.equals(otp)) throw new AppException(ErrorCode.OTP_INVALID);
        redisTemplate.opsForValue().set(redisKey, RedisKeyType.VALUE_AFTER_VERIFY.getType(), durationVerify, TimeUnit.SECONDS);
    }

    @Override
    public void deleteOtp(String email, String keyPrefix) {
        String redisKey = keyPrefix + email;
        redisTemplate.delete(email);
    }

    @Override
    public void validEmailBeforePersist(String email) {
        String redisKey = RedisKeyType.REGISTER.getRedisKeyPrefix() + email;
        String value = redisTemplate.opsForValue().get(redisKey);
        if (!RedisKeyType.VALUE_AFTER_VERIFY.getType().equals(value)) throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @Override
    public void sortTypeForSendOtp(EmailRequest request) {
        if (request.getType().equals(RedisKeyType.REGISTER.getType())) {
            sendOtp(request.getEmail(),
                    RedisKeyType.REGISTER.getRedisKeyPrefix());
        } else {
            sendOtp(request.getEmail(),
                    RedisKeyType.RESET.getRedisKeyPrefix());
        }
    }

    @Override
    public void sortTypeForVerifyOtp(OtpRequest request) {
        if (request.getType().equals(RedisKeyType.REGISTER.getType())) {
            validateOtp(request.getEmail(), request.getOtp(),
                    RedisKeyType.REGISTER.getRedisKeyPrefix());
        } else {
            validateOtp(request.getEmail(), request.getOtp(),
                    RedisKeyType.RESET.getRedisKeyPrefix());
        }
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}
