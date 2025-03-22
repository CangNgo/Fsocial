package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.dto.replyComment.ReplyCommentUpdateDTORequest;
import com.fsocial.postservice.entity.ReplyComment;
import com.fsocial.postservice.exception.AppCheckedException;

import java.io.IOException;

public interface ReplyCommentService {

    ReplyComment addReplyComment(ReplyCommentRequest request) throws AppCheckedException, IOException;

    ReplyComment updateReplyComment(ReplyCommentUpdateDTORequest request) throws AppCheckedException;

    String deleteReplyComment (String replyCommentId) throws AppCheckedException;

}
