package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<PostResponse> findByContentTextContaining(String content);
    List<PostResponse> findByContentTextContainingIgnoreCase(String content);
}
