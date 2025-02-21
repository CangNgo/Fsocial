package com.fsocial.postservice.dto;

import com.fsocial.postservice.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTORequest {
    String text;
    String HTMLText;
    MultipartFile[] media;
    Integer countLikes = 0;
}
