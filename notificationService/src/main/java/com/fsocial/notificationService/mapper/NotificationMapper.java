package com.fsocial.notificationService.mapper;

import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toEntity(NoticeRequest request);
    NotificationResponse toDto(Notification notification);
}
