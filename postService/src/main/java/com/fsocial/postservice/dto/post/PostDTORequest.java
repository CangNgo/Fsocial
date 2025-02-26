package com.fsocial.postservice.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTORequest {
    LocalDateTime createdAt = LocalDateTime.now();
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
    Integer countLikes = 0;
    Integer countComment = 0;
}
