package com.fsocial.notificationService.service.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.dto.response.ProfileNameResponse;
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
        log.info("Received NoticeRequest: OwnerId={}, PostId={}, CommentId={}, Type={}, Message={}",
                request.getOwnerId(), request.getPostId(), request.getCommentId(), request.getType(), request.getMessage());

        Notification notification = notificationMapper.toEntity(request);
        log.info("Mapped Notification Entity: {}", notification);

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Saved Notification: {}", savedNotification);

        return notificationMapper.toDto(savedNotification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(String userId) {
        List<Notification> notifications = notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId);
        log.info("Lấy toàn bộ thông báo thành công.");

        return notifications.stream().map(notification -> {
            NotificationResponse response = notificationMapper.toDto(notification);

            // Gọi ProfileService qua Feign Client
            ProfileNameResponse profileData = profileClient.getProfileByUserId(notification.getOwnerId());
            if (profileData != null) {
                response.setFirstName(profileData.getFirstName());
                response.setLastName(profileData.getLastName());
                response.setAvatar(profileData.getAvatar());
            }

            return response;
        }).toList();
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
        log.info("Received Kafka message: {}", response);

        // Lấy thông tin người nhận thông báo từ Profile Service
        var profile = profileClient.getProfileByUserId(response.getReceiverId());

        // Sử dụng postId từ response
        String postId = response.getPostId();
        String commentId = response.getCommentId();// Lấy postId từ thông báo Kafka

        // Tạo thông điệp thông báo
        String message = profile.getFirstName() + " " + profile.getLastName() + " " + response.getMessage();
        String notificationType = response.getTopic().equals("notice-comment") ? "COMMENT" : "LIKE";

        // Gọi phương thức tạo thông báo (Lưu vào CSDL)
        createNotification(NoticeRequest.builder()
                .ownerId(response.getOwnerId())  // OwnerId là bài viết (postId)
                .message(message)
                .type(notificationType)
                .postId(postId)
                .commentId(commentId)// Thêm postId vào thông báo
                .build());

        // Log thông báo đã nhận
        log.info("Kafka notification received: Topic={}, ReceiverId={}, Message={}, CommentId={}",
                response.getTopic(), response.getReceiverId(), message, commentId);
    }

}
