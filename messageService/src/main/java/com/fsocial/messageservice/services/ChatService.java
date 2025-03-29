package com.fsocial.messageservice.services;

import com.fsocial.messageservice.dto.ApiResponse;
import com.fsocial.messageservice.dto.request.ActionsRequest;
import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.MessageResponse;

public interface ChatService {
    MessageResponse cacheChatMessage(MessageRequest request);
    ApiResponse<?> handleChatActions(ActionsRequest request);
}
