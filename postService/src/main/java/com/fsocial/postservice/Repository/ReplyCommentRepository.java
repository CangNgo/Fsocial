package com.fsocial.postservice.Repository;

import com.fsocial.postservice.entity.ReplyComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment, String> {
}
