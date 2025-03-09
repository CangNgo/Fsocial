
package com.fsocial.messageservice.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "conversation")
public class Conversation {
    @Id
    String id; // mã cuộc trò chuyện
    String senderId; // Id người dùng gửi tin nhắn
    String receiverId; // Id người dùng nhận tin nhắn
    String firstName;
    String lastName;
    String avatar;
    LocalDateTime createAt = LocalDateTime.now(); // thời gian tạo cuộc trò chuyện
}
