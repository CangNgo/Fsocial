package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.entity.ReplyComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReplyCommentMapper {
    @Mapping(source = "commentId", target = "commentId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "text", target = "content.text")
    @Mapping(source = "HTMLText", target = "content.HTMLText")
    ReplyComment toEntity(ReplyCommentRequest request);
}

