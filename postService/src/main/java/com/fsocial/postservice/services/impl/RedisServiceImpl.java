package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.services.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisServiceImpl implements RedisService {
   RedisTemplate<String, String> redisTemplate;


   @Override
    public void saveData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void saveList(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public List<String> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void personalization(String userId, String value) {
         this.saveList("personalization_" + userId, value);
    }

    @Override
    public List<String> getPersonalization(String userId) {
       return this.getList("personalization_" + userId);
    }

}
