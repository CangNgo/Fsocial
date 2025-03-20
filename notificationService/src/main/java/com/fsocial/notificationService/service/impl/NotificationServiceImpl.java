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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    ProfileClient profileClient;
    RedisTemplate<String, Object> redisTemplate;
    SimpMessagingTemplate messagingTemplate;

    static final String PROFILE_CACHE_PREFIX = "profile:user:";
    static final int CACHE_DURATION_MINUTES = 10;

    @Override
    @Transactional
    public Notification createNotification(NoticeRequest request) {
        return notificationRepository.save(notificationMapper.toEntity(request));
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresentOrElse(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
            log.info("Thông báo đã được đánh dấu là đã đọc: Id={}", notificationId);
        }, () -> { throw new AppException(ErrorCode.NOT_FOUND); });
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapNotificationToResponse).toList();
    }

    private ProfileNameResponse getProfileFromCacheOrApi(String userId) {
        if (userId == null) throw new AppException(ErrorCode.NOT_FOUND);

        String cacheKey = PROFILE_CACHE_PREFIX + userId;

        ProfileNameResponse profile = (ProfileNameResponse) redisTemplate.opsForValue().get(cacheKey);
        if (profile != null) return profile;

        try {
            profile = profileClient.getProfileByUserId(userId);
            if (profile != null) {
                redisTemplate.opsForValue().set(cacheKey, profile, CACHE_DURATION_MINUTES, TimeUnit.MINUTES);
                return profile;
            }
        } catch (Exception e) {
            log.error("Lỗi khi tìm thông tin hồ sơ cho userId = {}: {}", userId, e.getMessage());
        }

        return null;
    }

    private NotificationResponse mapNotificationToResponse(Notification notification) {
        NotificationResponse response = notificationMapper.toDto(notification);
        ProfileNameResponse profile = getProfileFromCacheOrApi(notification.getReceiverId());
        if (profile != null) {
            response.setFirstName(profile.getFirstName());
            response.setLastName(profile.getLastName());
            response.setAvatar(profile.getAvatar());
        }
        return response;
    }

    @KafkaListener(topics = {"notice-comment", "notice-like"})
    private void handleKafkaNotification(NotificationRequest request) {
        if (request == null || request.getReceiverId() == null) {
            log.warn("Đã nhận được yêu cầu thông báo không hợp lệ");
            return;
        }
        ProfileNameResponse profile = getProfileFromCacheOrApi(request.getReceiverId());
        if (profile == null) {
            log.warn("Lỗi khi tìm thông tin hồ sơ cho userId = {}", request.getReceiverId());
            return;
        }
        String notificationType = "notice-comment".equals(request.getTopic()) ? "COMMENT" : "LIKE";
        Notification notification = createNotification(NoticeRequest.builder()
                .ownerId(request.getOwnerId())
                .type(notificationType)
                .postId(request.getPostId())
                .commentId(request.getCommentId())
                .receiverId(request.getReceiverId())
                .build());

        sendNotificationToUser(mapNotificationToResponse(notification));
    }

    private void sendNotificationToUser(NotificationResponse notification) {
        String userId = notification.getOwnerId();
        messagingTemplate.convertAndSend("/topic/notifications-" + userId, notification);
        log.info("Đã gửi thông báo qua WebSocket tới userId={}", userId);
    }
}