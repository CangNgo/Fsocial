package com.fsocial.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    String postId;
    String commentId;
    String ownerId;
    String receiverId;
    String message;
    String topic;
}
