package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{ $project: {countLikes : {$size: '$likes'}}}"
    })
    Integer countLikeByPost(String postId);

    boolean existsByIdAndLikes(String postId, String userId);
}
