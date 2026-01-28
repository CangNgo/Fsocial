package com.fsocial.postservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "relationships")
public class Relationship extends AbstractEntity<String> {
    @Field("follower")
    Set<String> listFollower = Set.of();
    @Field("following")
    Set<String> listFollowing = Set.of();
    @Field("user_id")
    String userId;
    @Field("is_public")
    Boolean isPublic;

    public Relationship(String userId){
        this.userId = userId;
    }

}
