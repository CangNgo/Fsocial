package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.services.OtpService;
import com.fsocial.accountservice.util.MailUtils;
import com.fsocial.accountservice.util.RedisUtils;
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

        if (keyPrefix.equals(RedisUtils.TYPE_REGISTER) || keyPrefix.equals(RedisUtils.TYPE_RESET)) {
            String redisKey = keyPrefix + email;
            redisTemplate.opsForValue().set(redisKey, otp, 5, TimeUnit.MINUTES);
            mailUtils.sendOtp(email, otp);
            log.info("OTP: {}", otp);
        } else {
            throw new AppException(ErrorCode.INVALID_TYPE_REQUEST);
        }
    }

    @Override
    public void validateOtp(String email, String otp, String keyPrefix) {
        String redisKey = keyPrefix + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);
        if (storedOtp == null || !storedOtp.equals(otp)) throw new AppException(ErrorCode.OTP_INVALID);
        redisTemplate.opsForValue().set(redisKey, RedisUtils.VALUE_AFTER_VERIFY, 1, TimeUnit.MINUTES);
    }

    @Override
    public void deleteOtp(String email) {
        redisTemplate.delete(email);
    }

    @Override
    public void validEmailBeforePersist(String email) {
        String checkOtpInRedis = redisTemplate.opsForValue().get(RedisUtils.REDIS_KEY_GET_REGISTER + email);
        if (checkOtpInRedis == null || !checkOtpInRedis.equals(RedisUtils.VALUE_AFTER_VERIFY)) throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    private String generateOtp() {
        return String.format("%04d", new Random().nextInt(10000));
    }
}
