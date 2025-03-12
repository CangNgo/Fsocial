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
import java.util.stream.Collectors;

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
        log.info("Received NoticeRequest: SenderId={}, PostId={}, CommentId={}, Type={}, Message={}",
                request.getSenderId(), request.getPostId(), request.getCommentId(), request.getType(), request.getMessage());

        // Tạo đối tượng Notification từ NoticeRequest
        Notification notification = new Notification();
        notification.setPostID(request.getPostId());
        notification.setCommentID(request.getCommentId());
        notification.setSenderId(request.getSenderId());
        notification.setReceiverId(request.getReceiverId());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setRead(false);  // Mới tạo thì chưa đọc

        // Lưu vào MongoDB
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Saved Notification: {}", savedNotification);

        // Lấy thông tin người gửi từ ProfileService
        ProfileNameResponse profileData = profileClient.getProfileByUserId(request.getSenderId());

        // Tạo NotificationResponse từ Notification và Profile thông tin
        return new NotificationResponse(
                savedNotification.getId(),
                savedNotification.getPostID(),
                savedNotification.getCommentID(),
                savedNotification.getSenderId(),
                savedNotification.getMessage(),
                savedNotification.isRead(),
                savedNotification.getType(),
                profileData != null ? profileData.getFirstName() : null, // firstName
                profileData != null ? profileData.getLastName() : null,  // lastName
                profileData != null ? profileData.getAvatar() : null,    // avatar
                savedNotification.getCreatedAt()

        );
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(String userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        log.info("Fetched all notifications for userId: {}", userId);

        return notifications.stream().map(notification -> {
            NotificationResponse response = notificationMapper.toDto(notification);

            // Lấy thông tin người gửi từ ProfileService
            ProfileNameResponse profileData = profileClient.getProfileByUserId(notification.getSenderId());
            if (profileData != null) {
                response.setFirstName(profileData.getFirstName());
                response.setLastName(profileData.getLastName());
                response.setAvatar(profileData.getAvatar());
            }

            return response;
        }).collect(Collectors.toList());
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

//    @Override
//    public Page<Notification> getNotificationsByUser(String userId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId, pageable);
//    }

    @KafkaListener(topics = {"notice-comment", "notice-like"})
    public void handleKafkaNotification(NotificationRequest response) {
        log.info("Received Kafka message: {}", response);

        // Lấy thông tin người nhận thông báo từ Profile Service
        ProfileNameResponse profile = profileClient.getProfileByUserId(response.getReceiverId());

        // Sử dụng postId từ response
        String postId = response.getPostId();
        String commentId = response.getCommentId();  // Lấy commentId từ thông báo Kafka

        // Tạo thông điệp thông báo
        String message = profile.getFirstName() + " " + profile.getLastName() + " " + response.getMessage();
        String notificationType = response.getTopic().equals("notice-comment") ? "COMMENT" : "LIKE";

        // Tạo thông báo và lưu vào MongoDB
        createNotification(NoticeRequest.builder()
                .senderId(response.getSenderId())  // OwnerId là bài viết (postId)
                .receiverId(response.getReceiverId())
                .message(message)
                .type(notificationType)
                .postId(postId)
                .commentId(commentId)  // Thêm postId và commentId vào thông báo
                .build());

        // Log thông báo đã nhận
        log.info("Kafka notification received: Topic={}, ReceiverId={}, Message={}, CommentId={}",
                response.getTopic(), response.getReceiverId(), message, commentId);
    }
}
