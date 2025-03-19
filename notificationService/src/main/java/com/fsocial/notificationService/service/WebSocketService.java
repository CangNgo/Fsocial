package com.fsocial.notificationService.service;

import com.fsocial.notificationService.dto.response.NotificationResponse;

public interface WebSocketService {
    void sendNotificationToUser(String userId, NotificationResponse notification);
}
