package com.fsocial.postservice.dto.replyComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCommentRequest {
    String commentId;
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
    int countLikes = 0;
    int countComments = 0;
}
