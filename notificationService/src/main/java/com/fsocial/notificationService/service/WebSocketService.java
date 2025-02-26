package com.fsocial.notificationService.service;

import com.fsocial.notificationService.entity.Notification;

public interface WebSocketService {
    void sendNotificationToUser(String userId, Notification notification);
}
