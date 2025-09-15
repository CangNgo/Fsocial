package com.fsocial.notificationService.service;

import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.AllNotificationResponse;
import com.fsocial.notificationService.entity.Notification;

public interface NotificationService {
    Notification createNotification(NoticeRequest request);
    void markAsRead(String notificationId);
    void markAllAsRead(String userId);
    AllNotificationResponse getNotificationsByUser(String userId, int page, int size);
    void deleteNotification(String notificationId);
}
