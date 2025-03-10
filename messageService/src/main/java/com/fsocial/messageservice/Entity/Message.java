package com.fsocial.messageservice.Entity;

import com.fsocial.messageservice.enums.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
@FieldDefaults(level = AccessLevel.PRIVATE)
@CompoundIndexes({
        @CompoundIndex(name = "idx_conversation_createAt", def = "{'conversationId': 1, 'createAt': -1}"),
        @CompoundIndex(name = "idx_conversation_isRead", def = "{'conversationId': 1, 'isRead': 1}"),
        @CompoundIndex(name = "idx_receiver_createAt", def = "{'receiver': 1, 'createAt': -1}")
})
public class Message {
    @Id
    String id;

    @Indexed
    String conversationId; // Tối ưu hóa truy vấn lấy tin nhắn theo cuộc trò chuyện

    MessageType type;

    @Indexed
    String receiver; // Hỗ trợ tìm kiếm tin nhắn theo người nhận

    String content;

    @Indexed(direction = IndexDirection.DESCENDING)
    LocalDateTime createAt = LocalDateTime.now(); // Tăng tốc sắp xếp tin nhắn mới nhất

    @Indexed
    boolean isRead = false; // Tăng tốc truy vấn tin nhắn chưa đọc
}
