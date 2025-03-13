package com.fsocial.messageservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingStatusRequest {
    String conversationId;
    String senderId;
    boolean isTyping;
}
