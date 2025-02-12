package com.fsocial.timelineservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("count_likes")
    Integer countLikes = 0;
    @Field("count_comments")
    Integer countComments = 0;

}
