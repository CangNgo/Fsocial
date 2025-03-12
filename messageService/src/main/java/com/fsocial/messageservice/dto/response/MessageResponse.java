package com.fsocial.messageservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MessageResponse {
    String conversationId;
    String receiverId;
    String content;
    boolean isRead;
    LocalDateTime createAt;
}
