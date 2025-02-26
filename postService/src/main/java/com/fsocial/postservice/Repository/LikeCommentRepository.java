package com.fsocial.postservice.Repository;

import com.fsocial.postservice.entity.LikeComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeCommentRepository extends MongoRepository<LikeComment, String> {
}
