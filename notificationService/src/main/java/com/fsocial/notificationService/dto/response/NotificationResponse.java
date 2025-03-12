package com.fsocial.notificationService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NotificationResponse {
    String id;
    String postId;
    String commentId;
    String senderId;        // Người gửi thông báo (trước đây là userId)
    String message;
    boolean isRead;
    String type;
    String firstName;
    String lastName;
    String avatar;
    LocalDateTime createdAt;
}
