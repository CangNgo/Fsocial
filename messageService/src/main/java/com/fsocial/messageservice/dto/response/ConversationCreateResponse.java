package com.fsocial.messageservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ConversationCreateResponse {
    String id;
    String receiverId;
    String firstName;
    String lastName;
    String avatar;
}
