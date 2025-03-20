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

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class CacheServiceImpl implements CacheService {
    AccountClient accountClient;
    RedisTemplate<String, Boolean> booleanRedisTemplate;
    ConversationRepository conversationRepository;

    @Override
    public void validateUser(String userId) {
        String cacheKey = "user:" + userId;
        Boolean isCached = booleanRedisTemplate.opsForValue().get(cacheKey);

        if (Boolean.TRUE.equals(isCached)) {
            booleanRedisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
            return;
        }

        if (!accountClient.validUserId(userId)) {
            log.error("Xác thực thất bại: userId {}", userId);
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }

        booleanRedisTemplate.opsForValue().set(cacheKey, true, 5, TimeUnit.MINUTES);
    }

    @Override
    public void ensureConversationExists(String conversationId) {
        String cacheKey = "conversation:" + conversationId;

        if (Boolean.TRUE.equals(booleanRedisTemplate.opsForValue().get(cacheKey))) {
            booleanRedisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
            return;
        }

        if (!conversationRepository.existsById(conversationId)) {
            log.warn("Không tìm thấy cuộc trò chuyện: {}", conversationId);
            throw new AppException(ErrorCode.CONVERSATION_NOT_EXIST);
        }

        booleanRedisTemplate.opsForValue().set(cacheKey, true, 5, TimeUnit.MINUTES);
    }
}
