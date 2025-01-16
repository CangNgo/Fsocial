package com.fsocial.postservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

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

    public Post(String userId, Content content) {
        this.userId = userId;
        this.content = content;
        this.countLikes = 0;
    }
}
