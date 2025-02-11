package com.fsocial.profileservice.entity;

<<<<<<< HEAD
import lombok.*;
=======
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> c865938a7cd9ca1c50772e3e22c0b2e435a40bf2
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

@Node("account_profile")
@Getter
@Setter
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

    @Property
    String bio;

    @Property
    String avatar;

    @Property
    String banner;

    @Property
    int gender;

<<<<<<< HEAD
    @Property
    String address;

    @Property("dob")
    LocalDate dob;

    @Property("created_at")
=======
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

>>>>>>> c865938a7cd9ca1c50772e3e22c0b2e435a40bf2
    LocalDate createdAt = LocalDate.now();

    @Property("updated_at")
    LocalDate updatedAt;
}
