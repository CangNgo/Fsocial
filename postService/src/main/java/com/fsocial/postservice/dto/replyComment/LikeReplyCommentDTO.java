package com.fsocial.postservice.dto.replyComment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LikeReplyCommentDTO {
    @NotBlank(message = "Replycomment Id không được bỏ trống")
    String replyCommentId;
    @NotBlank(message = "User Id không được bỏ trống")
    String userId;
}
