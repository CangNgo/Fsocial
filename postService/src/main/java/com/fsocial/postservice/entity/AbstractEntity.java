package com.fsocial.postservice.entity;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

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

    @MongoId(FieldType.STRING)
    private String id = UUID.randomUUID().toString();

    @CreatedBy
    @Field("created_by")
    private T createdBy;

    @LastModifiedBy
    @Field("updated_by")
    private T updatedBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Field("updated_at")
    private LocalDateTime updatedAt;

}