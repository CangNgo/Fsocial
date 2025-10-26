package com.fsocial.notificationService.entity;

import com.fsocial.notificationService.enums.ChannelType;
import com.fsocial.notificationService.enums.NotifyTo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends AbstractEntity<String> {
    String title;
    String message;
    String deeplink;
    NotifyTo notifyTo;
    ChannelType channel;
    String[] email;
    String ownerId;
    boolean isRead;
    String type;
    String[] receiverId;
}
