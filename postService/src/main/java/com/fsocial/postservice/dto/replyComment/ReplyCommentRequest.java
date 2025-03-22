package com.fsocial.postservice.dto.replyComment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyCommentRequest {
    String commentId;
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
}
