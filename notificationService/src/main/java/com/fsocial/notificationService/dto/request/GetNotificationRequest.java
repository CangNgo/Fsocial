package com.fsocial.notificationService.dto.request;

import com.fsocial.notificationService.enums.ChannelType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetNotificationRequest {
    String ownerId;
    ChannelType channel;
    int page = 0 ;
    int limit = 10;
}
