package com.fsocial.postservice.dto;

import com.fsocial.postservice.entity.Content;
import com.fsocial.postservice.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    Post post;
    String userId;
    ContentDTO content;
    int countLikes;
    int countReplyComment;
    boolean reply;
}
