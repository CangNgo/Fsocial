package com.fsocial.messageservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MessageRequest {
    String receiverId;
    String content;
    String conversationId;
}
