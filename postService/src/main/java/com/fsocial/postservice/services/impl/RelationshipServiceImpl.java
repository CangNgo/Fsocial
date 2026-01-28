package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.entity.Relationship;
import com.fsocial.postservice.exception.AppUnCheckedException;
import com.fsocial.postservice.exception.StatusCode;
import com.fsocial.postservice.repository.RelationshipRepository;
import com.fsocial.postservice.services.RelationshipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {
    RelationshipRepository relationshipRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    RedisTemplate<String, String> redisTemplate;

    private static final int TTL_SECONDS = 600; // time live of Redis in Seconds

    @Override
    @Transactional
    public void follow(String userId, String targetId) {
        updateFollowRelation(userId, targetId, true);
        log.info("User {} followed user {}", userId, targetId);
        cacheFollowing(userId, targetId);
    }

    @Override
    public void unfollow(String userId, String targetId) {
        updateFollowRelation(userId, targetId, false);
        log.info("User {} unfollowed user {}", userId, targetId);
        removeCacheFollowing(userId, targetId);
    }

    @Override
    public boolean isFollowing(String userId, String targetId) {
        String followingKey = "following:" + targetId;
        Boolean isMember = redisTemplate.opsForSet().isMember(followingKey, userId);
        return isMember != null && isMember || getFollowers(targetId).contains(userId);
    }

    @Override
    public Set<String> getFollowers(String userId) {
        return getCachedFollowData("follower:" + userId, userId, true);
    }

    @Override
    public Set<String> getFollowing(String userId) {
        return getCachedFollowData("following:" + userId, userId, false);
    }

    private void updateFollowRelation(String userId, String targetId, boolean isFollow) {
        Relationship target = relationshipRepository.findById(targetId)
            .orElseGet(() -> new Relationship(targetId));

        Relationship follower = relationshipRepository.findById(userId)
            .orElseGet(() -> new Relationship(userId));

        if (isFollow) {
            target.getListFollower().add(userId) ;
            follower.getListFollowing().add(targetId) ;
        } else {
           target.getListFollowing().remove(userId);
           follower.getListFollower().remove(targetId);
        }
        relationshipRepository.save(target);
        relationshipRepository.save(follower);
    }

    private Set<String> getCachedFollowData(String cacheKey, String targetId, boolean isFollower) {
        Set<String> data = redisTemplate.opsForSet().members(cacheKey);
        if (data != null && !data.isEmpty()) return data;

        data = fetchDataFromDB(targetId, isFollower);
        if (!data.isEmpty()) updateRedis(cacheKey, data);
        return data;
    }

    private Set<String> fetchDataFromDB(String targetId, boolean isFollower) {
        Relationship follower = relationshipRepository.findById(targetId)
            .orElseThrow(() -> new AppUnCheckedException(StatusCode.NOT_FOUND));
        return isFollower ? follower.getListFollower() : follower.getListFollowing();
    }

    private void updateRedis(String key, Set<String> data) {
        redisTemplate.opsForSet().add(key, data.toArray(new String[0]));
        redisTemplate.expire(key, TTL_SECONDS, TimeUnit.SECONDS);
    }

    private void cacheFollowing(String followerId, String targetId) {
        String key = "following:" + followerId;
        redisTemplate.opsForSet().add(key, targetId);
        redisTemplate.expire(key, TTL_SECONDS, TimeUnit.SECONDS);
    }

    private void removeCacheFollowing(String followerId, String targetId) {
        redisTemplate.opsForSet().remove("following:" + followerId, targetId);
    }
}
