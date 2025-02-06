package com.fsocial.postservice.services;

import com.fsocial.postservice.entity.Comment;

import java.util.List;

public interface CommentService{
    Comment addComment(Comment comment);
    List<Comment> getComments(String postId);
}
