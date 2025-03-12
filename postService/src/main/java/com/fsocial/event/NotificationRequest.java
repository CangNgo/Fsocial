package com.fsocial.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    private String postId;
    private String commentId;
    private String senderId;
    private String receiverId;
    private String message;
    private String type;
    private String topic;
}
