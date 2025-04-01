package com.fsocial.messageservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MessageRequest {
    String messageId;
    String conversationId;
    String receiverId;
    String content;
    List<String> images;
    LocalDateTime createAt = LocalDateTime.now();
}
