package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Comment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId);

    @Query("{ '_id': ?0 }")
    @Update("{'$addToSet': {'likes': ?1}}")
    void addLikeComment(String commentId, String userId);

    @Query("{ '_id': ?0 }")
    @Update("{'$pull': {'likes': ?1}}")
    void removeLikeComment(String commentId, String userId);

    Integer countByPostId(String postId);

    @Aggregation(pipeline = {
            "{'$match': {'_id': ?0}}",
            "{'$project': {'totalLikes': {'$size': '$likes'}}}"
    })
    Integer countLikes(String commentId);

    boolean existsByIdAndLikes(String id, String userId);

}
