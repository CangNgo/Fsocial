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
import java.util.concurrent.TimeUnit;

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
    public Notification createNotification(NoticeRequest request) {
        Notification notification = notificationMapper.toEntity(request);

        log.info("Đã lưu thông báo.");
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUser(String userId) {
        List<Notification> notifications = notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId);

        List<NotificationResponse> responseList = notifications.stream()
                .map(this::updateNotificationToResponse)
                .toList();

        log.info("Lấy toàn bộ Thông báo thành công.");
        return responseList;
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
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.stream().map(this::updateNotificationToResponse).toList();
    }

    private NotificationResponse updateNotificationToResponse(Notification notification) {
        NotificationResponse response = notificationMapper.toDto(notification);

        String receiverId = notification.getReceiverId();
        log.info(">>>>>>>> TEST RECEIVERID: {}", receiverId);
        getAndSetSenderProfile(response, receiverId);
        return response;
    }

    private void getAndSetSenderProfile(NotificationResponse response, String receiverId) {
        var profile = profileClient.getProfileByUserId(receiverId);
        log.info(">>>>>>>> TEST PROFILE: {}", profile);
        String firstName = profile.getFirstName();
        String lastName = profile.getLastName();
        String avatar = profile.getAvatar();

        response.setFirstName(firstName);
        response.setLastName(lastName);
        response.setAvatar(avatar);
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
                .postId(response.getPostId())
                .commentId(response.getCommentId())
                .receiverId(response.getReceiverId())
                .build());
    }
}
