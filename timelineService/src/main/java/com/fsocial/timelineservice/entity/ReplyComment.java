package com.fsocial.timelineservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Field("likes")
    List<String> likes = new ArrayList<>();

    @Field("created_datetime")
    LocalDateTime created_datetime;

}
