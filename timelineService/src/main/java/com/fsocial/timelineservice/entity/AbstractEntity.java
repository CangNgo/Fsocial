package com.fsocial.timelineservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Document
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity<T extends Serializable> implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    @CreatedBy
    @Field("created_by")
    private T createdBy;

    @LastModifiedBy
    @Field("updated_by")
    private T updatedBy;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}