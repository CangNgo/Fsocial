package com.fsocial.postservice.dto.post;

import com.fsocial.postservice.dto.ContentDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {
    String id;
    String userId;
    ContentDTO content;
    Integer countLikes = 0;
    Integer countComments = 0;
}
