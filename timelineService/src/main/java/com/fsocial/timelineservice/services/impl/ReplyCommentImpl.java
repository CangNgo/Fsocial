package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.repository.ReplyCommentRepository;
import com.fsocial.timelineservice.entity.ReplyComment;
import com.fsocial.timelineservice.services.ReplyCommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyCommentImpl implements ReplyCommentService {

    ReplyCommentRepository replyCommentRepository;
    @Override
    public List<ReplyComment> getReplyCommentsByCommentId(String commentId) {
        return replyCommentRepository.findReplyCommentsByCommentId(commentId);
    }
}
