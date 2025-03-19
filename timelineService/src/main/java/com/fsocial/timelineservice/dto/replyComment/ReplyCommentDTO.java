package com.fsocial.timelineservice.dto.replyComment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReplyCommentDTO {
    String commentId;

    com.fsocial.timelineservice.dto.post.ContentDTO content;

    String  userId;
}
