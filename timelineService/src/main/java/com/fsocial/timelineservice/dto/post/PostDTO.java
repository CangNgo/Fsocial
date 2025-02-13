package com.fsocial.timelineservice.dto.post;

import com.fsocial.timelineservice.entity.Content;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {
    String id;
    String userId;
    Content content;
    Integer countLikes;

}
