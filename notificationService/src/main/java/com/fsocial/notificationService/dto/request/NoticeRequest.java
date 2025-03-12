package com.fsocial.notificationService.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoticeRequest {
    private String postId;
    private String commentId;
    private String senderId;
    private String receiverId;
    private String message;
    private String type;
}
