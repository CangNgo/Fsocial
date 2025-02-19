package com.fsocial.postservice.dto.replyComment;

import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.entity.Content;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "comment")
@Builder
public class ReplyCommentDTO {
    String commentId;

    ContentDTO content;

    String  userId;

    int countLikes;

    int countReplyComment;
}
