package com.fsocial.processorservice.entity.primaryDatabase;

import com.fsocial.processorservice.entity.AbstractEntity;
import com.fsocial.processorservice.entity.Content;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "post")
@TypeAlias("write")
public class PostWrite extends AbstractEntity<String> {
    @Field("user_id")
    String userId;
    @Field("content")
    Content content;
    @Field("count_likes")
    Integer countLikes = 0;

}
