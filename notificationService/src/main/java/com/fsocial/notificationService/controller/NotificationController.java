package com.fsocial.notificationService.controller;

import com.fsocial.notificationService.dto.ApiResponse;
import com.fsocial.notificationService.dto.response.AllNotificationResponse;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.enums.ResponseStatus;
import com.fsocial.notificationService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("/notice")
public class NotificationController {

    NotificationService notificationService;

    @GetMapping("/{userId}")
    public ApiResponse<AllNotificationResponse> getNotifications(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        AllNotificationResponse response = notificationService.getNotificationsByUser(userId, page, size);
        return ApiResponse.buildApiResponse(response, ResponseStatus.SUCCESS);
    }

    @PutMapping("/{notificationId}")
    public ApiResponse<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @PutMapping("/mark-read-all")
    public ApiResponse<Void> markAllAsRead() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.markAllAsRead(userId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<Void> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.buildApiResponse(null, ResponseStatus.SUCCESS);
    }
}
