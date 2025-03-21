package com.fsocial.notificationService.service;

import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    Notification createNotification(NoticeRequest request);
    void markAsRead(String notificationId);
    List<NotificationResponse> getNotificationsByUser(String userId, int page, int size);
    void deleteNotification(String notificationId);
}
