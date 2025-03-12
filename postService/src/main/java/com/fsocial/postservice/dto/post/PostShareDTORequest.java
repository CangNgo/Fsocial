package com.fsocial.postservice.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostShareDTORequest {
    @NotBlank(message = "Id người dùng không được để trống")
    String userId;
    String text;
    String HTMLText;
    @NotBlank(message = "originPost người dùng không được để trống")
    String originPostId;
}
