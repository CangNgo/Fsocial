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
@Document(collection = "reply_comment")
public class ReplyComment extends AbstractEntity<String>{

    @Field("comment_id")
    String commentId;

    @Field("content")
    Content content;

    @Field("userId")
    String  userId;

    @Field("count_likes")
    int countLikes;

    @Field("count_reply_comment")
    int countReplyComment;

}
