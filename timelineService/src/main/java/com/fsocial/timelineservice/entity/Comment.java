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
@Document(collection = "comment")
@Builder
public class Comment extends AbstractEntity<String>{
    @Field("postId")
    String postId;
    @Field("userId")
    String userId;
    @Field("content")
    Content content;
    @Field("likes")
    List<String> likes = new ArrayList<>();
    @Field("created_datetime")
    LocalDateTime createDatetime = LocalDateTime.now();
    @Field("reply")
    boolean Reply;
}
