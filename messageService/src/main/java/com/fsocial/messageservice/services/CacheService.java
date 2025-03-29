package com.fsocial.messageservice.services;

public interface CacheService {
    void validateUser(String userId);
    void ensureConversationExists(String conversationId);
}
