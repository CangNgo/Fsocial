package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.notification.NoticeRequest;
import com.fsocial.postservice.dto.notification.NotificationResponse;
import com.fsocial.postservice.repository.httpClient.NotificationClient;
import com.fsocial.postservice.services.NotificaitonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificaitonService {

    NotificationClient notificationClient;

    @Override
    public NotificationResponse createNotification(NoticeRequest notificationRequest) {
        return notificationClient.createNotification(notificationRequest).getData();
    }
}
