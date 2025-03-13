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
import org.springframework.data.redis.core.RedisTemplate;
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
        // Lưu thông báo vào cơ sở dữ liệu (không cần lưu firstName, lastName, avatar)
        Notification notification = notificationMapper.toEntity(request);
        notification.setPostId(request.getPostId());
        notification.setCommentId(request.getCommentId());


        log.info("Đã lưu thông báo.");
        return notificationRepository.save(notification);
    }

    private NotificationResponse updateNotificationResponse(NotificationResponse response, String senderId) {
        // Lấy thông tin profile của người nhận từ API (profileClient)
        var profile = profileClient.getProfileByUserId(senderId);
        String firstName = profile.getFirstName();
        String lastName = profile.getLastName();
        String avatar = profile.getAvatar();  // Lấy avatar từ ProfileNameResponse

        // Chuyển Notification thành NotificationResponse
//        NotificationResponse response = notificationMapper.toDto(notification);

        // Thêm các trường firstName, lastName và avatar vào response (chỉ khi trả về)
        response.setFirstName(firstName);
        response.setLastName(lastName);
        response.setAvatar(avatar);

        // Ghi log thông tin
//        log.info("Thông báo: OwnerId={}, Type={}, Message={}, PostId={}, CommentId={}, FirstName={}, LastName={}, Avatar={}",
//                request.getOwnerId(), request.getType(), request.getMessage(), request.getPostId(), request.getCommentId(),
//                firstName, lastName, avatar);

        // Trả về thông báo đã được bổ sung thêm thông tin
//        return response;
        return null;
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

        Notification notification = createNotification(NoticeRequest.builder()
                .ownerId(response.getOwnerId())
                .message(message)
                .type(notificationType)
                .postId(response.getPostId())
                .commentId(response.getCommentId())
                .build());
    }
}
