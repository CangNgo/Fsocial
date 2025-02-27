package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {
    boolean existsByPostIdAndUserIds(String postId, String userId);
}
