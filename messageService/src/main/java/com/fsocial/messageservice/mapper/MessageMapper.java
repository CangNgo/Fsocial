package com.fsocial.messageservice.mapper;

import com.fsocial.messageservice.Entity.Message;
import com.fsocial.messageservice.dto.response.LastMessage;
import com.fsocial.messageservice.dto.response.MessageResponse;
import com.fsocial.messageservice.dto.request.MessageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "messageId", target = "id")
    Message toEntity(MessageRequest request);

    @Mapping(source = "id", target = "messageId")
    MessageResponse toMessageResponse(Message entity);

    LastMessage toLastMessage(Message entity);
}
