package com.fsocial.postservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "comment")
@Builder
public class Comment extends AbstractEntity<String>{
    @Field("postId")
    String postId;
    @Field("userId")
    String userId;
    @Field("content")
    Content content;
    @Field("count_likes")
    int countLikes;
    @Field("count_reply_comment")
    int countReplyComment;
    @Field("reply")
    boolean reply;
}
