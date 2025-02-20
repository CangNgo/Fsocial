package com.fsocial.timelineservice.services;


import com.fsocial.timelineservice.entity.ReplyComment;

import java.util.List;

public interface ReplyCommentService {
    List<ReplyComment> getReplyCommentsByCommentId(String commentId);
}
