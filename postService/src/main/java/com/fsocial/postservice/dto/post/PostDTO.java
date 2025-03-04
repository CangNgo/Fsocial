package com.fsocial.postservice.dto.post;

import com.fsocial.postservice.dto.ContentDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {
    String id;
    String userId;
    LocalDateTime createdAt;
    ContentDTO content;
    Integer countLikes = 0;
    Integer countComments = 0;
}
