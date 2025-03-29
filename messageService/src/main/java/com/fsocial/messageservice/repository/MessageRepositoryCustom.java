package com.fsocial.messageservice.repository;

public interface MessageRepositoryCustom {
    void updateMessagesAsRead(String conversationId, String userId);
}
