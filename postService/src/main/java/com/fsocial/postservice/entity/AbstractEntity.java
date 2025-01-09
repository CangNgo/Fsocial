package com.fsocial.postservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractEntity<T extends Serializable> implements Serializable {
    @MongoId(FieldType.STRING)
    private T id;

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

    public AbstractEntity() {
        if (id == null && UUID.class.isAssignableFrom(getGenericType())) {
            this.id = generateId();
        }
    }

    // Method để tạo id (có thể tuỳ chỉnh nếu muốn hỗ trợ kiểu id khác)
    @SuppressWarnings("unchecked")
    private T generateId() {
        if (UUID.class.isAssignableFrom(getGenericType())) {
            return (T) UUID.randomUUID();
        }
        return null; // Nếu không phải UUID, không generate giá trị
    }

    // Lấy kiểu generic của `T` để kiểm tra trong runtime
    @SuppressWarnings("unchecked")
    private Class<T> getGenericType() {
        return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
}