package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.ReplyComment;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment, String> {

    boolean existsByIdAndLikes(String id, String userId);

    // Methods from timelineService
    List<ReplyComment> findReplyCommentsByCommentId(String commentId);

    @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
            "{'$match': {'_id': ?0}}",
            "{'$project': {'totalLikes': {'$size': '$likes'}}}"
    })
    Integer countLike(String replyCommentId);
}
