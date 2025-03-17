package com.fsocial.notificationService.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoticeRequest {
    String ownerId;
    String type;
    String postId;
    String commentId;
    String receiverId;
}
