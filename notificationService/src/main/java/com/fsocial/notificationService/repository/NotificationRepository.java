package com.fsocial.notificationService.repository;

import com.fsocial.notificationService.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
//    List<Notification> findByOwnerIdOrderByCreatedAtDesc(String userId);
//    // Lấy danh sách thông báo theo userId, phân trang và sắp xếp theo createdAt giảm dần
//    Page<Notification> findByOwnerIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(String receiverId);
}
