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
    LocalDateTime createDatetime = LocalDateTime.now();
    @NotBlank(message = "Id người dùng không được để trống")
    String userId;
    String text;
    String HTMLText;
    MultipartFile[] media;
}
