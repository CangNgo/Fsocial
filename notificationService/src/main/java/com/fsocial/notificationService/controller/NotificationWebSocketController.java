package com.fsocial.notificationService.controller;

import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationWebSocketController {
    NotificationService notificationService;

    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public List<NotificationResponse> getNotifications(@RequestParam String userId) {
        return notificationService.getNotificationsByUser(userId, 0, 10);
    }
}
