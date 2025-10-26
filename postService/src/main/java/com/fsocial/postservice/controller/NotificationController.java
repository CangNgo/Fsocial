package com.fsocial.postservice.controller;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.dto.notification.NoticeRequest;
import com.fsocial.postservice.dto.notification.NotificationResponse;
import com.fsocial.postservice.services.NotificaitonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/notification")
public class NotificationController {

    NotificaitonService notificationService;

    @PostMapping
    public ResponseEntity<Response> createNotification(@RequestBody NoticeRequest notificationRequest) {

        return ResponseEntity.ok(Response.builder()
                .statusCode(200)
                .data(notificationService.createNotification(notificationRequest))
                .message("Create notification success")
                .build());
    }

}
