package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    @Query("{'_id': ?0}")
    @Update("{ '$addtoSet' : { 'likes' : ?1}}")
    void addLike(String postId, String userId) ;

    @Query("{'_id': ?0}")
    @Update("{ '$pull':  {'likes' : ?1}}")
    void removeLike(String postId, String userId) ;

    @Aggregation(pipeline = {
            "{$match:  {_id:  ?0}}",
            "{ $project: {countLikes : {$size:  '$likes'}}}"
    })
    Integer countLikeByPost(String postId);

    boolean existsByIdAndLikes(String postId, String userId);
}
