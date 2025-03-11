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
    String ownerId;
    String message;
    boolean isRead;
    String type;
    LocalDateTime createdAt;
}
