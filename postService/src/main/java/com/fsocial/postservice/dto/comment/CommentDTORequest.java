package com.fsocial.postservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTORequest {
    String postId;
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
    int countLikes = 0;
    int countComments = 0;
}
