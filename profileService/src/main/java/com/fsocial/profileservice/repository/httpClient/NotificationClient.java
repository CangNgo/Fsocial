package com.fsocial.profileservice.repository.httpClient;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.request.NoticeRequest;
import com.fsocial.profileservice.dto.response.NotificationResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification", url = "${app.services.notification}", path = "/notification")
public interface NotificationClient {
    @PostMapping(value = "/notice",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<NotificationResponse> createNotification(@Valid @RequestBody NoticeRequest notificationRequest);
}
