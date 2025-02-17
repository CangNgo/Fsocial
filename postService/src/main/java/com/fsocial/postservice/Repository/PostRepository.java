package com.fsocial.postservice.Repository;

import com.fsocial.postservice.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, String> {
}
