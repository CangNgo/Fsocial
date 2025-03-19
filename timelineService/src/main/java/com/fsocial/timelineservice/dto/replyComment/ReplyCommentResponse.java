package com.fsocial.timelineservice.dto.replyComment;

import com.fsocial.timelineservice.entity.Content;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReplyCommentResponse {
    String id;
    String userId;
    Content content;
    Integer countLikes;
    String firstName;
    String lastName;
    String avatar;
    LocalDateTime creat_datetime;
}
