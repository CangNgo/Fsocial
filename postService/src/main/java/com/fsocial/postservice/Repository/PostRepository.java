package com.fsocial.postservice.Repository;

import com.fsocial.postservice.entity.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, String> {

}
