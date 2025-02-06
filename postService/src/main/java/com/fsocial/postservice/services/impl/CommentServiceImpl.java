package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.Repository.CommentRepository;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.services.CommentService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    @Override
    public Comment addComment(Comment comment) {

        comment.setCreatedAt(LocalDateTime.now());
        comment.setCreatedBy(comment.getUserId());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepository.findByPostId(postId);
    }
}
