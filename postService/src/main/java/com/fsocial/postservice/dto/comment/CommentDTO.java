package com.fsocial.postservice.dto.comment;

import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
