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
    public Notification createNotification(NoticeRequest request) {
        // Lưu thông báo vào cơ sở dữ liệu (không cần lưu firstName, lastName, avatar)
        Notification notification = notificationMapper.toEntity(request);
        notification.setPostId(request.getPostId());
        notification.setCommentId(request.getCommentId());


        log.info("Đã lưu thông báo.");
        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUser(String userId) {
        // Lấy danh sách thông báo từ cơ sở dữ liệu
        List<Notification> notifications = notificationRepository.findByOwnerIdOrderByCreatedAtDesc(userId);

        // Duyệt qua danh sách các thông báo và cập nhật thông tin người thực hiện hành động (receiverId)
        List<NotificationResponse> responseList = notifications.stream()
                .map(notification -> {
                    // Chuyển đổi Notification thành NotificationResponse
                    NotificationResponse response = notificationMapper.toDto(notification);

                    // Lấy receiverId từ thông báo, đây là người thực hiện hành động (like hoặc comment)
                    String receiverId = notification.getReceiverId(); // Sử dụng receiverId là người thực hiện hành động

                    // Lấy thông tin người thực hiện hành động từ ProfileClient (dùng receiverId ở đây)
                    var profile = profileClient.getProfileByUserId(receiverId);  // Lấy thông tin người thực hiện hành động
                    String firstName = profile.getFirstName();
                    String lastName = profile.getLastName();
                    String avatar = profile.getAvatar();  // Lấy avatar của người thực hiện hành động

                    // Cập nhật các thông tin vào NotificationResponse
                    response.setFirstName(firstName);
                    response.setLastName(lastName);
                    response.setAvatar(avatar);

                    return response;
                })
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
                .receiverId(response.getReceiverId())
                .build());
    }
}
