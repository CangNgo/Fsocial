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
    String ownerId;
    boolean isRead;
    String type;
    LocalDateTime createdAt;
    String postId;
    String commentId;
    String firstName;
    String lastName;
    String avatar;
}
