package com.fsocial.notificationService.service.impl;

import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.service.WebSocketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketServiceImpl implements WebSocketService {
    SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(String userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }
}
