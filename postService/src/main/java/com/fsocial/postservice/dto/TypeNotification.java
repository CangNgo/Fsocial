package com.fsocial.postservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeNotification {
    String ownerId;
    String receiverId;
    String message;
}
