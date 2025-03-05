package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.entity.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<PostResponse> findByContentTextContaining(String content);
    List<PostResponse> findByContentTextContainingIgnoreCase(String content);
    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{ $project: {countLikes : {$size: '$likes'}}}"
    })
    Integer countLikeByPost(String postId);

    boolean existsByIdAndLikes(String postId, String userId);
}
