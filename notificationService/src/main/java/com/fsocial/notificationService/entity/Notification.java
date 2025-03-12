package com.fsocial.notificationService.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    String id;
    String postID;
    String commentID;
    String senderId;
    String receiverId;

    String message;
    boolean isRead;
    String type;
    LocalDateTime createdAt = LocalDateTime.now();
}
