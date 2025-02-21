package com.fsocial.postservice.dto.post;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Content invalid")
    String text;
    @NotBlank(message = "Content invalid")
    String HTMLText;
    MultipartFile[] media;
    Integer countLikes = 0;
    Integer countComment = 0;
}
