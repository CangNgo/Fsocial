package com.fsocial.notificationService.repository;

public interface CustomNotificationRepository {
    void markAllAsReadByUserId(String userId);
}
