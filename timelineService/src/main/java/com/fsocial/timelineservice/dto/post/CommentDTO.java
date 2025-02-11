package com.fsocial.timelineservice.dto.post;

import com.fsocial.timelineservice.entity.Post;
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
