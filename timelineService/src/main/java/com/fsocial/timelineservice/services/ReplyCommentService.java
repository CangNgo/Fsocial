package com.fsocial.timelineservice.services;


import com.fsocial.timelineservice.dto.replyComment.ReplyCommentResponse;
import com.fsocial.timelineservice.entity.ReplyComment;

import java.util.List;

public interface ReplyCommentService {
    List<ReplyCommentResponse> getReplyCommentsByCommentId(String commentId);
}
