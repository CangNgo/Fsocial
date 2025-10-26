package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.notification.NoticeRequest;
import com.fsocial.postservice.dto.notification.NotificationResponse;

public interface NotificaitonService {
    NotificationResponse createNotification(NoticeRequest notificationRequest);
}
