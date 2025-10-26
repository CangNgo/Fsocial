package com.fsocial.notificationService.repository;

import com.fsocial.notificationService.dto.response.NotificationResponse;
import com.fsocial.notificationService.entity.Notification;
import com.fsocial.notificationService.enums.ChannelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, CustomNotificationRepository {
    List<Notification> findByOwnerIdOrderByCreatedAtDesc(String userId);

    // Lấy danh sách thông báo theo userId, phân trang và sắp xếp theo createdAt giảm dần
    Page<Notification> findByOwnerIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    @Query(value = "{ 'ownerId': ?0, 'read': false }", count = true)
    long countUnreadNotificationsByOwnerId(String userId);

    Page<NotificationResponse> getNotificationByOwnerIdAndChannel(String ownerId, ChannelType channel, Pageable pageable);
}
