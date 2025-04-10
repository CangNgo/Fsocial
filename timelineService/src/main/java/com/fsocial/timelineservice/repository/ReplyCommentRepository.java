package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.entity.ReplyComment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment, String> {
    List<ReplyComment> findReplyCommentsByCommentId(String commentId);
    @Aggregation(pipeline = {
            "{'$match': {'_id': ?0}}",
            "{'$project': {'totalLikes': {'$size': '$likes'}}}"
    })
    Integer countLike(String replyCommentId);
}
