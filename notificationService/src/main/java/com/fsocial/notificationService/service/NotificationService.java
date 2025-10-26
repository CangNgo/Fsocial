package com.fsocial.notificationService.service;

import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.AllNotificationResponse;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.enums.ChannelType;
import com.fsocial.notificationService.exception.AppCheckedException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    NotificationResponse createNotification(NoticeRequest request) throws AppCheckedException;
    void markAsRead(String notificationId);
    void markAllAsRead(String userId);
    AllNotificationResponse getNotificationsByUser(String userId, int page, int size);
    void deleteNotification(String notificationId);

    List<NotificationResponse> getNotificationByOwnerIdAndChannel_Inbox(String ownerId, ChannelType channel, int page, int limit);
}
