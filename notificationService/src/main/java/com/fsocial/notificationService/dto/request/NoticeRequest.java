package com.fsocial.notificationService.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoticeRequest {
    String ownerId;
    String message;
    String type;
    String postId;
    String commentId;
}
