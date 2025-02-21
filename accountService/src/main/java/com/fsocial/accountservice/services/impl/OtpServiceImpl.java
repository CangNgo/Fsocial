package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.EmailRequest;
import com.fsocial.accountservice.dto.request.account.OtpRequest;
import com.fsocial.accountservice.enums.RedisKeyType;
import com.fsocial.accountservice.enums.ValidErrorCode;
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

        String redisKey = keyPrefix + email;
        redisTemplate.opsForValue().set(redisKey, otp, durationSend, TimeUnit.MINUTES);
        mailUtils.sendOtp(email, otp);
        log.info("OTP: {}", otp);
    }

    @Override
    public void validateOtp(String email, String otp, String keyPrefix) {
        String redisKey = keyPrefix + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);

        if (storedOtp == null) {
            log.warn("OTP không tồn tại hoặc đã hết hạn cho email: {}", email);
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        if (!storedOtp.equals(otp)) {
            log.warn("OTP không hợp lệ cho email: {}", email);
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        redisTemplate.opsForValue().set(redisKey, RedisKeyType.VALUE_AFTER_VERIFY.getType(), durationVerify, TimeUnit.SECONDS);
    }

    @Override
    public void deleteOtp(String email, String keyPrefix) {
        String redisKey = keyPrefix + email;
        redisTemplate.delete(redisKey);
    }

    @Override
    public void validEmailBeforePersist(String email) {
        String redisKey = RedisKeyType.REGISTER.getRedisKeyPrefix() + email;
        String value = redisTemplate.opsForValue().get(redisKey);

        if (!RedisKeyType.VALUE_AFTER_VERIFY.getType().equals(value)) {
            log.warn("Email chưa được xác thực: {}", email);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public void sortTypeForSendOtp(EmailRequest request) {
        String email = request.getEmail();
        String keyPrefix = checkKeyPrefix(request.getType());

        sendOtp(email, keyPrefix);
    }

    @Override
    public void sortTypeForVerifyOtp(OtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();
        String keyPrefix = checkKeyPrefix(request.getType());


        validateOtp(email, otp, keyPrefix);
    }

    private String checkKeyPrefix(String type) {
        if (!type.equals(RedisKeyType.REGISTER.getType()) && !type.equals(RedisKeyType.RESET.getType())) {
            log.error("Sai loại yêu cầu ở Request: {}", type);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return type.equals(RedisKeyType.REGISTER.getType())
                ? RedisKeyType.REGISTER.getRedisKeyPrefix()
                : RedisKeyType.RESET.getRedisKeyPrefix();
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}
