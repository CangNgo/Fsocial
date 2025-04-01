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
    @Mapping(target = "images", ignore = true)
    Message toEntity(MessageRequest messageRequest);

    @Mapping(source = "messageId", target = "id")
    Message toEntity(MessageResponse messageResponse);

    @Mapping(source = "id", target = "messageId")
    MessageResponse toMessageResponse(Message entity);

    @Mapping(target = "images", ignore = true)
    MessageResponse toMessageResponse(MessageRequest request);

    LastMessage toLastMessage(Message entity);

}
