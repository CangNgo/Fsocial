package com.fsocial.postservice.dto.Attachments;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentDTO {
    String id;
    String publicId;
    String url;
    String resourceType;
    String fileType;
    String size;
    String ownerId;
    LocalDateTime createdAt;
}
