package com.fsocial.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    String ownerId;
    String receiverId;
    String message;
    String topic;
    String postId;  // Thêm trường postId
    String commentId;
}
