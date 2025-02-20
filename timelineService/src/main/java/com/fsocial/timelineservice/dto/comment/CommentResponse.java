package com.fsocial.timelineservice.dto.comment;

import com.fsocial.timelineservice.entity.Content;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String userId;
    Content content;
    Integer countLikes;
    Integer countReplyComments;
    String userName;
    String avatar;
    LocalDateTime createdAt;
    boolean reply;
}
