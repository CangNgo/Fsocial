package com.fsocial.notificationService.controller;

import com.fsocial.notificationService.dto.ApiResponse;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.enums.ResponseStatus;
import com.fsocial.notificationService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {

    NotificationService notificationService;

    @GetMapping("/all/{userId}")
    public ApiResponse<List<NotificationResponse>> getNotificationsByUser(@PathVariable String userId) {
        log.info("Getting notifications for user: {}", userId);
        return ApiResponse.buildApiResponse(notificationService.getNotificationsByUser(userId),
                ResponseStatus.SUCCESS);
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<NotificationResponse>> getNotifications(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        return ApiResponse.buildApiResponse(notificationService.getNotificationsByUser(userId, page, size),
                ResponseStatus.SUCCESS);
    }

    @PutMapping("/{notificationId}")
    public ApiResponse<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }
}
