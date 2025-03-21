package com.fsocial.postservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
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
@Document(collection = "post")
@SuperBuilder
public class Post extends AbstractEntity<String> {
    @Field("user_id")
    String userId;

    @Field("content")
    Content content;

    @Field("likes")
    List<String> likes = new ArrayList<>();

    @Field("create_datetime")
    LocalDateTime createDatetime = LocalDateTime.now();
    //share
    @Field("origin_post")
    String originPostId;

    @Field("is_share")
    @Builder.Default
    Boolean isShare = false;

    @Field("status")
    @Builder.Default
    Boolean status = true;
}
