package com.fsocial.messageservice.Entity;

import com.fsocial.messageservice.enums.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    String id;
    String conversationId;
    MessageType type;
    String sender;
    String receiver;
    String content;
    LocalDateTime createAt = LocalDateTime.now();
    boolean isRead = false;
}
