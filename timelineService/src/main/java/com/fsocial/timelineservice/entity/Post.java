package com.fsocial.timelineservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "post")
public class Post extends AbstractEntity<String> {
    @Field("user_id")
    String userId;
    @Field("content")
    Content content;
    @Field("likes")
    List<String> likes;
    @Field("create_datetime")
    LocalDateTime createDatetime = LocalDateTime.now();
    //share
    @Field("origin_post")
    String originPostId;
    @Field("is_share")
    Boolean isShare = false;

    @Field("status")
    Boolean status = true;
}
