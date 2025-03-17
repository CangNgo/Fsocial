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

    private static final String PROFILE_CACHE_PREFIX = "profile:user:";
    private static final int CACHE_DURATION_MINUTES = 10;

    @Override
    @Transactional
    public Notification createNotification(NoticeRequest request) {
        Notification notification = notificationMapper.toEntity(request);

        log.info("Đã lưu thông báo.");
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        notification.setRead(true);
        notificationRepository.save(notification);
        log.info("Thông báo được đánh dấu là đã đọc: Id={}", notificationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId, pageable);

        // Map để lưu profile tránh gọi API nhiều lần
        Map<String, ProfileNameResponse> profileCache = new HashMap<>();

        return notifications.stream().map(notification -> {
            NotificationResponse response = notificationMapper.toDto(notification);
            String receiverId = notification.getReceiverId();

            // Kiểm tra xem profile đã có trong Map chưa
            ProfileNameResponse profile = profileCache.computeIfAbsent(receiverId, this::getProfileFromCacheOrApi);

            if (profile != null) {
                response.setFirstName(profile.getFirstName());
                response.setLastName(profile.getLastName());
                response.setAvatar(profile.getAvatar());
            }
            return response;
        }).toList();
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

        String receiverId = notification.getReceiverId();
        ProfileNameResponse profile = getProfileFromCacheOrApi(receiverId);

        if (profile != null) {
            response.setFirstName(profile.getFirstName());
            response.setLastName(profile.getLastName());
            response.setAvatar(profile.getAvatar());
        }

        return response;
    }

    @KafkaListener(topics = {"notice-comment", "notice-like"})
    private void handleKafkaNotification(NotificationRequest request) {
        try {
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

            NoticeRequest noticeRequest = NoticeRequest.builder()
                    .ownerId(request.getOwnerId())
                    .type(notificationType)
                    .postId(request.getPostId())
                    .commentId(request.getCommentId())
                    .receiverId(request.getReceiverId())
                    .build();

            Notification notification = createNotification(noticeRequest);
            NotificationResponse response = notificationMapper.toDto(notification);

            // Gửi thông báo qua WebSocket
            sendNotificationToUser(request.getOwnerId(), response);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý thông báo Kafka: {}", e.getMessage());
        }
    }

    public void sendNotificationToUser(String userId, NotificationResponse notification) {
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Đã gửi thông báo qua WebSocket tới userId={}", userId);
    }
}
