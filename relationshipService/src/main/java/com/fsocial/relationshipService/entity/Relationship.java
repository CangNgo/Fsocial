package com.fsocial.relationshipService.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "relationships")
public class Relationship {
    @Id
    String targetId;
    Set<String> listFollower = Set.of();
    Set<String> listFollowing = Set.of();
    int totalFollower = 0;
    int totalFollowing = 0;

    public Relationship(String targetId) {
        this.targetId = targetId;
    }
}
