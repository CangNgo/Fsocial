package com.fsocial.profileservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

@Node("account_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicUpdate
public class AccountProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("user_id")  // Using snake_case for Neo4j property names
    String userId;

    @Property("first_name")
    String firstName;

    @Property("last_name")
    String lastName;

    @Property("bio")
    String bio;

    @Property("avatar")
    String avatar;

    @Property("banner")
    String banner;

    @Property("gender")
    int gender;

    @Property("address")
    String address;

    @Property("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    @Property("updated_at")
    LocalDate updatedAt;

    LocalDate createdAt = LocalDate.now();
    boolean isPublic = true;
}
