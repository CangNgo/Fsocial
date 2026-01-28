package com.fsocial.postservice.dto.comment;

import com.fsocial.postservice.entity.Content;
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
    String firstName;
    String lastName;
    String avatar;
    LocalDateTime createDatetime;
    boolean reply;
    boolean like;
}
