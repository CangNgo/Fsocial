package com.fsocial.timelineservice.Repository;

import com.fsocial.timelineservice.entity.ReplyComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment, String> {
    List<ReplyComment> findReplyCommentsByCommentId(String commentId);
}
