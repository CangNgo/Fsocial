package com.fsocial.messageservice.mapper;

import com.fsocial.messageservice.Entity.Conversation;
import com.fsocial.messageservice.dto.request.ConversationRequest;
import com.fsocial.messageservice.dto.response.ConversationCreateResponse;
import com.fsocial.messageservice.dto.response.ConversationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    Conversation toEntity(ConversationRequest request);
    ConversationCreateResponse toConversationCreateResponse(Conversation entity);
    ConversationResponse toConversationResponse(Conversation entity);
}
