package com.fsocial.messageservice.services;

import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.LastMessage;
import com.fsocial.messageservice.dto.response.MessageListResponse;
import com.fsocial.messageservice.dto.response.MessageResponse;

import java.util.List;
import java.util.Map;

public interface MessageService {
    MessageListResponse findChatMessagesBetweenUsers(String conversationId, int page, String userId);
    MessageResponse saveChatMessage(MessageRequest request);
    void markMessagesAsRead(String conversationId, String userId);
    void deleteMessagesByConversationId(String conversationId);
    void deleteMessage(String messageId);
    Map<String, LastMessage> findLastMessagesForConversations(List<String> conversationIds);
}

