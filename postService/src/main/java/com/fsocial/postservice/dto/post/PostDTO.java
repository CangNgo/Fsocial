package com.fsocial.postservice.dto.post;

import com.fsocial.postservice.dto.ContentDTO;
import com.fsocial.postservice.entity.Content;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {
    String id;
    String userId;
    LocalDateTime createDatetime;
    ContentDTO content;
    String originPostId;
    List<String> likes = new ArrayList<>();
    Boolean isShare = false;
    Boolean status = true;
}
