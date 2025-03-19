package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.ReplyComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment, String> {

    boolean existsByIdAndLikes(String id, String userId);
}
