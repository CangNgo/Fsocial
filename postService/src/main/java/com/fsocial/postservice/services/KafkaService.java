package com.fsocial.postservice.services;

import com.fsocial.event.NotificationRequest;
import com.fsocial.postservice.enums.MessageNotice;

public interface KafkaService {
    void sendNotification(String ownerId, String userId, MessageNotice messageNotice, String postId, String commentId);
}
