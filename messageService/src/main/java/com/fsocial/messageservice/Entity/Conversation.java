
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "conversation")
@CompoundIndexes({
        @CompoundIndex(name = "idx_participants", def = "{'participants': 1}"),
        @CompoundIndex(name = "idx_createAt", def = "{'createAt': -1}")
})
public class Conversation {
    @Id
    String id;

    @Indexed
    List<String> participants; // 🆕 Danh sách người tham gia cuộc trò chuyện

    @Indexed(direction = IndexDirection.DESCENDING)
    LocalDateTime createAt = LocalDateTime.now(); // Tối ưu sắp xếp cuộc trò chuyện mới nhất
}
