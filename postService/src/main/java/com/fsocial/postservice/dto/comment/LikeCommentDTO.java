package com.fsocial.postservice.dto.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeCommentDTO {
    String commentId;
    String userId;
}
