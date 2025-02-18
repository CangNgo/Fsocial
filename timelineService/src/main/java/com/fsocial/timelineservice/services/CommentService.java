package com.fsocial.timelineservice.services;

import com.fsocial.timelineservice.dto.comment.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getComments(String postId);
}
