package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Comment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId);

    Integer countByPostId(String postId);

    @Aggregation(pipeline = {
            "{'$match': {'_id': ?0}}",
            "{'$project': {'totalLikes': {'$size': '$likes'}}}"
    })
    Integer countLikes(String commentId);

    boolean existsByIdAndLikes(String id, String userId);

    boolean existsById(String id);

    @Query(value = "{'_id': ?0}", fields = "{'postId': 1}")
    Optional<String> findPostIdByCommentId(String id);
}
