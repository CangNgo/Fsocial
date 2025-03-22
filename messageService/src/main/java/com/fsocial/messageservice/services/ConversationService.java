package com.fsocial.messageservice.services;

import com.fsocial.messageservice.Entity.Conversation;
import com.fsocial.messageservice.dto.request.ConversationRequest;
import com.fsocial.messageservice.dto.response.ConversationCreateResponse;
import com.fsocial.messageservice.dto.response.ConversationResponse;

import java.util.List;

public interface ConversationService {
    Conversation createConversation(ConversationRequest request);
    ConversationCreateResponse responseToClient(ConversationRequest request);
    void deleteConversation(String conversationId);
    List<ConversationResponse> getAllConversations(String userId);
}
