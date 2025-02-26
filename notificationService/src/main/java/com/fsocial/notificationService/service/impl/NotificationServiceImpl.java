package com.fsocial.notificationService.service.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.enums.ErrorCode;
import com.fsocial.notificationService.exception.AppException;
import com.fsocial.notificationService.mapper.NotificationMapper;
import com.fsocial.notificationService.repository.NotificationRepository;
import com.fsocial.notificationService.repository.httpClient.ProfileClient;
import com.fsocial.notificationService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    ProfileClient profileClient;

    @Override
    @Transactional
    public NotificationResponse createNotification(NoticeRequest request) {
        Notification notification = notificationRepository.save(notificationMapper.toEntity(request));
        log.info("Thông báo: OwnerId={}, Type={}, Message={}", request.getOwnerId(), request.getType(), request.getMessage());
        return notificationMapper.toDto(notification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(String userId) {
        List<Notification> notifications =  notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId);
        log.info("Lấy toàn bộ Thông báo thành công.");
        return notifications.stream().map(notificationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        notification.setRead(true);
        notificationRepository.save(notification);
        log.info("Notification marked as read: Id={}", notificationId);
    }

    @Override
    public Page<Notification> getNotificationsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId, pageable);
    }

    @KafkaListener(topics = {"notice-comment", "notice-like"})
    public void handleKafkaNotification(NotificationRequest response) {
        var profile = profileClient.getProfileByUserId(response.getReceiverId());
        String message = profile.getFirstName() + " " + profile.getLastName() + " " + response.getMessage();
        String notificationType = response.getTopic().equals("notice-comment") ? "COMMENT" : "LIKE";

        createNotification(NoticeRequest.builder()
                .ownerId(response.getOwnerId())
                .message(message)
                .type(notificationType)
                .build());

        log.info("Kafka notification received: Topic={}, ReceiverId={}, Message={}", response.getTopic(), response.getReceiverId(), message);
    }
}
