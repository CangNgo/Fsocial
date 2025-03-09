package com.fsocial.messageservice.mapper;

import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.response.LastMessage;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.dto.request.MessageRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toEntity(MessageRequest request);
    MessageResponse toMessageResponse(Message entity);
    LastMessage toLastMessage(Message entity);
}
