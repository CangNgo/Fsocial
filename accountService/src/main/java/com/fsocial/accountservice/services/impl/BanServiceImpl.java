package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.services.BanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BanServiceImpl implements BanService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Override
    public void ban(String token) {
        this.redisTemplate.opsForValue().set("banned:" + token, token, 1 , TimeUnit.DAYS);
    }

    @Override
    public void unBan(String token) {
        this.redisTemplate.delete("banned:" + token);
    }
}
