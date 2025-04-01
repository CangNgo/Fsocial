package com.fsocial.messageservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    String messageId;
    String conversationId;
    String receiverId;
    String content;
    Map<String, String> images;
    boolean isRead;
    LocalDateTime createAt;
}
