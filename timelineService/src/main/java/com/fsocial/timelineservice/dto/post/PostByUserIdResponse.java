package com.fsocial.timelineservice.dto.post;

import com.fsocial.timelineservice.entity.Content;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostByUserIdResponse {
    String id;
    String userId;
    Content content;
    Integer countLikes;
    Integer countComments;
    String displayName;
    String avatar;
    LocalDateTime createDatetime;
    boolean isLike;
}
