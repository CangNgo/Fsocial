package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{ $project: {countLikes : {$size: '$likes'}}}"
    })
    Integer countLikeByPost(String postId);
    boolean existsByIdAndLikes(String postId, String userId);
    List<Post> findByUserId(String userId);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'user_id': 1 }")
    Optional<String> findUserIdById(String id);
}
