package com.fsocial.timelineservice.Repository;

import com.fsocial.timelineservice.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, String> {
}
