package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Integer countCommentsByPostId(String postId);
    List<Comment> findCommentsByPostId(String postId);
}
