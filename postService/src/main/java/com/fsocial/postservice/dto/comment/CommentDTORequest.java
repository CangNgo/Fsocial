package com.fsocial.postservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTORequest {
    LocalDateTime createdAt = LocalDateTime.now();
    String postId;
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
    int countLikes = 0;
    int countComments = 0;
}
