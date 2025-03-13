package com.fsocial.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NotificationRequest {
    String ownerId;
    String receiverId;
    String message;
    String topic;
     String postId;  // Thêm trường postId
     String commentId;
}
