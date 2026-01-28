package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.services.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RedisServiceImpl implements RedisService {
   RedisTemplate<String, String> redisTemplate;


   @Override
    public void saveData(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.warn("Redis operation failed (saveData): {} - Key: {}", e.getMessage(), key);
        }
    }

    @Override
    public String getData(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis operation failed (getData): {} - Key: {}", e.getMessage(), key);
            return null;
        }
    }

    @Override
    public void saveList(String key, String value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.warn("Redis operation failed (saveList): {} - Key: {}", e.getMessage(), key);
        }
    }

    @Override
    public List<String> getList(String key) {
        try {
            List<String> result = redisTemplate.opsForList().range(key, 0, -1);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            log.warn("Redis operation failed (getList): {} - Key: {}", e.getMessage(), key);
            return new ArrayList<>();
        }
    }

    @Override
    public void personalization(String userId, String value) {
         this.saveList("personalization_" + userId, value);
    }

    @Override
    public List<String> getPersonalization(String userId) {
       return this.getList("personalization_" + userId);
    }

    // Methods from timelineService
    @Override
    public void viewed(String userId, String value) {
        this.saveList("viewed_post_" + userId, value);
    }

    @Override
    public List<String> getViewed(String userId) {
        return this.getList("viewed_post_" + userId);
    }

    @Override
    public void cleaerViewed(String userId) {
        try {
            redisTemplate.delete("viewed_post_" + userId);
        } catch (Exception e) {
            log.warn("Redis operation failed (cleaerViewed): {} - UserId: {}", e.getMessage(), userId);
        }
    }

    @Override
    public void viewedFollowing(String userId, String postId) {
         this.saveList("viewed_post_following_" + userId, postId);
    }

    @Override
    public List<String> getViewedFollowing(String userId) {
        return this.getList("viewed_post_following_" + userId);
    }

    @Override
    public void clearViewedFollowing(String userId) {
        try {
            redisTemplate.delete("viewed_post_following_" + userId);
        } catch (Exception e) {
            log.warn("Redis operation failed (clearViewedFollowing): {} - UserId: {}", e.getMessage(), userId);
        }
    }

}
