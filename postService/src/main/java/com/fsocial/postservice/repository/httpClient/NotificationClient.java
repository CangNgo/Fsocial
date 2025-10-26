package com.fsocial.postservice.repository.httpClient;

import com.fsocial.postservice.dto.ApiResponse;
import com.fsocial.postservice.dto.notification.NoticeRequest;
import com.fsocial.postservice.dto.notification.NotificationResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification", url = "${app.services.notification}", path = "/notification")
public interface NotificationClient {
    @PostMapping(value = "/notice",  consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<NotificationResponse> createNotification(NoticeRequest notificationRequest);
}
