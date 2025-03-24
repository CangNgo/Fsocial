package com.fsocial.timelineservice.dto.complaint;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintDTO {
    String postId;
    String userId;
    String complaintType;
    String termOfService;
    LocalDateTime dateTime ;
    boolean readding;
}
