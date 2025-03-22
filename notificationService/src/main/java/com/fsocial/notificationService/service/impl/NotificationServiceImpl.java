package com.fsocial.notificationService.service.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.notificationService.dto.request.NoticeRequest;
import com.fsocial.notificationService.dto.response.AllNotificationResponse;
import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.dto.response.ProfileNameResponse;
import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.enums.ErrorCode;
import com.fsocial.notificationService.enums.TopicKafka;
import com.fsocial.notificationService.exception.AppException;
import com.fsocial.notificationService.mapper.NotificationMapper;
import com.fsocial.notificationService.repository.NotificationRepository;
import com.fsocial.notificationService.repository.httpClient.ProfileClient;
import com.fsocial.notificationService.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    static final int CACHE_DURATION_MINUTES = 10; // 10 phút

    @Override
    @Transactional
    public Notification createNotification(NoticeRequest request) {
        return notificationRepository.save(notificationMapper.toEntity(request));
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        if (notificationId == null) throw new AppException(ErrorCode.NOT_NULL);

        notificationRepository.findById(notificationId).ifPresentOrElse(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
            log.info("Thông báo đã được đánh dấu là đã đọc: Id={}", notificationId);
        }, () -> { throw new AppException(ErrorCode.NOT_FOUND); });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(String userId) {
        if (userId == null) throw new AppException(ErrorCode.NOT_NULL);

        notificationRepository.markAllAsReadByUserId(userId);
        log.info("Tất cả thông báo của userId={} đã được đánh dấu là đã đọc", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public AllNotificationResponse getNotificationsByUser(String userId, int page, int size) {
        if (userId == null) throw new AppException(ErrorCode.NOT_NULL);

        Pageable pageable = PageRequest.of(page, size);
        List<NotificationResponse> notificationResponse = notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapNotificationToResponse).toList();
        long numberNotificationUnread = notificationRepository.countUnreadNotificationsByOwnerId(userId);

        return AllNotificationResponse.builder()
                .notifications(notificationResponse)
                .unreadCount((int) numberNotificationUnread)
                .build();
    }

    @Override
    @Transactional
    public void deleteNotification(String notificationId) {
        if (notificationId == null) throw new AppException(ErrorCode.NOT_NULL);

        if (!notificationRepository.existsById(notificationId)) {
            log.warn("Không tìm thấy thông báo với id: {}", notificationId);
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        notificationRepository.deleteById(notificationId);
        log.info("Đã xoá thông báo với id: {}", notificationId);
    }

    private ProfileNameResponse getProfileFromCacheOrApi(String userId) {
        return Optional.ofNullable(userId)
                .map(id -> PROFILE_CACHE_PREFIX + id)
                .map(cacheKey -> Optional.ofNullable((ProfileNameResponse) redisTemplate.opsForValue().get(cacheKey))
                        .orElseGet(() -> fetchAndCacheProfile(userId, cacheKey)))
                .orElseThrow(() -> new AppException(ErrorCode.NOT_NULL));
    }

    private ProfileNameResponse fetchAndCacheProfile(String userId, String cacheKey) {
        try {
            ProfileNameResponse profile = profileClient.getProfileByUserId(userId);
            if (profile != null) {
                redisTemplate.opsForValue().set(cacheKey, profile, CACHE_DURATION_MINUTES, TimeUnit.MINUTES);
                return profile;
            }
        } catch (Exception e) {
            log.error("Lỗi khi tìm thông tin hồ sơ cho userId = {}: {}", userId, e.getMessage());
        }
        throw new AppException(ErrorCode.NOT_FOUND);
    }

    private NotificationResponse mapNotificationToResponse(Notification notification) {
        NotificationResponse response = notificationMapper.toDto(notification);
        response.setRead(notification.isRead());
        ProfileNameResponse profile = getProfileFromCacheOrApi(notification.getReceiverId());
        if (profile != null) {
            response.setFirstName(profile.getFirstName());
            response.setLastName(profile.getLastName());
            response.setAvatar(profile.getAvatar());
        }
        return response;
    }

    @KafkaListener(topics = "#{T(com.fsocial.notificationService.enums.TopicKafka).getAllTopics()}")
    private void handleKafkaNotification(NotificationRequest request,
                                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        if (!isValidRequest(request)) return;

        Optional<ProfileNameResponse> profileOpt = Optional.ofNullable(getProfileFromCacheOrApi(request.getReceiverId()));
        if (profileOpt.isEmpty()) {
            log.warn("Lỗi khi tìm thông tin hồ sơ cho userId = {}", request.getReceiverId());
            return;
        }

        TopicKafka notificationType = TopicKafka.fromTopic(topic);
        Notification notification = createNotification(buildNoticeRequest(request, notificationType));
        sendNotificationToUser(notification);
    }

    private boolean isValidRequest(NotificationRequest request) {
        if (request == null || request.getReceiverId() == null) {
            log.warn("Đã nhận được yêu cầu thông báo không hợp lệ");
            return false;
        }
        return true;
    }

    private NoticeRequest buildNoticeRequest(NotificationRequest request, TopicKafka notificationType) {
        NoticeRequest.NoticeRequestBuilder builder = NoticeRequest.builder()
                .ownerId(request.getOwnerId())
                .type(notificationType.name())
                .receiverId(request.getReceiverId());

        if (notificationType != TopicKafka.FOLLOW) {
            builder.postId(request.getPostId())
                    .commentId(request.getCommentId());
        }

        return builder.build();
    }

    private void sendNotificationToUser(Notification notification) {
        NotificationResponse response = mapNotificationToResponse(notification);
        String userId = notification.getOwnerId();
        messagingTemplate.convertAndSend("/topic/notifications-" + userId, response);
        log.info("Đã gửi thông báo qua WebSocket tới userId={}", userId);
    }
}