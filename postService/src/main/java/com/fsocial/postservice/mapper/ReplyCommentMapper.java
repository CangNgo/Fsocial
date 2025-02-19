package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.replyComment.ReplyCommentRequest;
import com.fsocial.postservice.entity.ReplyComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReplyCommentMapper {
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "countLikes", target = "countLikes")
    @Mapping(source = "countComments", target = "countReplyComment")
    @Mapping(source = "text", target = "content.text")
    @Mapping(source = "HTMLText", target = "content.HTMLText")
    ReplyComment toEntity(ReplyCommentRequest request);
}

