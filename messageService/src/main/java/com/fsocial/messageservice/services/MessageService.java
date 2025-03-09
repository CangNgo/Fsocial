package com.fsocial.messageservice.services;

import com.fsocial.messageservice.dto.request.MessageRequest;
import com.fsocial.messageservice.dto.response.LastMessage;
import com.fsocial.messageservice.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageResponse> findChatMessagesBetweenUsers(String conversationId, int page);
    MessageResponse saveChatMessage(MessageRequest request);
    void markMessagesAsRead(List<String> messageIds);
    LastMessage findLastMessageByConversationId(String conversationId);
    void deleteMessagesByConversationId(String conversationId);
    void deleteMessage(String messageId);
    List<LastMessage> findLastMessagesForConversations(List<String> conversationIds);
}

