package com.fsocial.messageservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarkReadResponse {
    String conversationId;
    String readerId;
    LocalDateTime timestamp = LocalDateTime.now();
}
