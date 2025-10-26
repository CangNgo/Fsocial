package com.fsocial.notificationService.dto.request;

import com.fsocial.notificationService.entity.AbstractEntity;
import com.fsocial.notificationService.enums.ChannelType;
import com.fsocial.notificationService.enums.NotifyTo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoticeRequest {
    @NotBlank(message = "Title is require")
    String title;
    String message;
    String deeplink;
    @NotBlank(message = "NotifyTo is require")
    NotifyTo notifyTo;
    @NotBlank(message = "Channel is require")
    ChannelType channel;
    String[] email;
    @NotBlank
    String ownerId;
    boolean isRead;
    String type;
    String[] receiverId;
}
