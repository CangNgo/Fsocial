package com.fsocial.notificationService.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fsocial.notificationService.enums.ChannelType;
import com.fsocial.notificationService.enums.NotifyTo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    String id;
    String title;
    String message;
    String deeplink;
    String[] email;
    @NotBlank
    String ownerId;
    boolean isRead;
    String type;
    String[] receiverId;
}
