package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.comment.CommentDTORequest;
import com.fsocial.postservice.entity.Comment;
import com.fsocial.postservice.exception.AppCheckedException;

import java.io.IOException;
import java.util.List;

public interface CommentService{
    Comment addComment(CommentDTORequest comment) throws IOException, AppCheckedException;

    boolean toggleLikeComment(String commentId, String userId);

    Integer countLike(String commentId, String userId);

}
