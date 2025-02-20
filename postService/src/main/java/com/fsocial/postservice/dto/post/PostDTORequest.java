package com.fsocial.postservice.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTORequest {
    LocalDateTime createdAt;
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
    Integer countLikes = 0;
    Integer countComment = 0;
}
