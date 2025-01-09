package com.fsocial.postservice.dto;

import com.fsocial.postservice.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    String userId;
    Content content;
    Integer countLikes = 0;

}
