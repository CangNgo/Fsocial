package com.fsocial.notificationService.service.impl;

import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.service.WebSocketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
    SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(String userId, NotificationResponse notification) {
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Đã gửi thông báo qua WebSocket tới userId={}", userId);
    }
}
