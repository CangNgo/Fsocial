package com.fsocial.notificationService.controller;

import com.fsocial.event.NotificationRequest;
import com.fsocial.notificationService.dto.response.ApiResponse;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.enums.ResponseStatus;
import com.fsocial.notificationService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/all/{userId}")
    public ApiResponse<List<NotificationResponse>> getNotificationsByUser(@PathVariable String userId) {
        log.info("Getting notifications for user: {}", userId);
        return buildResponse(notificationService.getNotificationsByUser(userId));
    }

//    @GetMapping("/{userId}")
//    public ApiResponse<Page<Notification>> getNotifications(
//            @PathVariable String userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return buildResponse(notificationService.getNotificationsByUser(userId, page, size));
//    }

    @PutMapping("/{notificationId}")
    public ApiResponse<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return buildResponse(null);
    }

    private <T> ApiResponse<T> buildResponse(T data) {
        return ApiResponse.<T>builder()
                .statusCode(ResponseStatus.SUCCESS.getCODE())
                .message(ResponseStatus.SUCCESS.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }
}
