package com.cangngo.apigateway.service.impl;

import com.cangngo.apigateway.service.BanService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanServiceImpl implements BanService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isBan(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("banned:" + token));
    }
}
