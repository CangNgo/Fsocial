
package com.fsocial.messageservice.Entity;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "conversation")
@CompoundIndexes({
        @CompoundIndex(name = "idx_sender_receiver", def = "{'senderId': 1, 'receiverId': 1}", unique = true),
        @CompoundIndex(name = "idx_createAt", def = "{'createAt': -1}")
})
public class Conversation {
    @Id
    String id;

    @Indexed
    String senderId;  // Tìm kiếm theo senderId

    @Indexed
    String receiverId; // Tìm kiếm theo receiverId

    String firstName;
    String lastName;
    String avatar;

    @Indexed(direction = IndexDirection.DESCENDING)
    LocalDateTime createAt = LocalDateTime.now(); // Tối ưu sắp xếp cuộc trò chuyện mới nhất
}
