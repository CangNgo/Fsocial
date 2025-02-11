package com.fsocial.timelineservice.dto.post;

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
    MultipartFile[] media;
    Integer countLikes = 0;
}
