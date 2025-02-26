package com.fsocial.timelineservice.Repository;

import com.fsocial.timelineservice.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, String> {
    boolean existsByPostIdAndUserId(String postId, String userId);
}
