package com.fsocial.postservice.dto.replyComment;

import com.fsocial.postservice.dto.ContentDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

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
