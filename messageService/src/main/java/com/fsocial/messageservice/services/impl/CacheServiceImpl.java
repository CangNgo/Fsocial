package com.fsocial.messageservice.services.impl;

import com.fsocial.messageservice.enums.ErrorCode;
import com.fsocial.messageservice.exception.AppException;
import com.fsocial.messageservice.repository.ConversationRepository;
import com.fsocial.messageservice.repository.httpClient.AccountClient;
import com.fsocial.messageservice.services.CacheService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class CacheServiceImpl implements CacheService {
    AccountClient accountClient;
    RedisTemplate<String, Boolean> booleanRedisTemplate;
    RedisTemplate<String, String> stringRedisTemplate;
    ConversationRepository conversationRepository;


    private static final long CACHE_EXPIRATION = 5; // 5 minutes

    @Override
    public void validateUser(String userId) {
        Objects.requireNonNull(userId, ErrorCode.NOT_NULL.getMessage());

        String cacheKey = "user:" + userId;
        Boolean isCached = booleanRedisTemplate.opsForValue().get(cacheKey);

        if (isCached(cacheKey)) return;

        if (!accountClient.validUserId(userId)) {
            log.error("Xác thực thất bại: userId {}", userId);
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }

        cacheValue(cacheKey);
    }

    @Override
    public void ensureConversationExists(String conversationId) {
        Objects.requireNonNull(conversationId, ErrorCode.NOT_NULL.getMessage());

        String cacheKey = "conversation:" + conversationId;

        if (isCached(cacheKey)) return;

        if (!conversationRepository.existsById(conversationId)) {
            log.warn("Không tìm thấy cuộc trò chuyện: {}", conversationId);
            throw new AppException(ErrorCode.CONVERSATION_NOT_EXIST);
        }

        cacheValue(cacheKey);
    }

    private boolean isCached(String key) {
        if (Boolean.TRUE.equals(booleanRedisTemplate.opsForValue().get(key))) {
            booleanRedisTemplate.expire(key, CACHE_EXPIRATION, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    private void cacheValue(String key) {
        booleanRedisTemplate.opsForValue().set(key, true, CACHE_EXPIRATION, TimeUnit.MINUTES);
    }
}
