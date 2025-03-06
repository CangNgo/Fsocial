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
public class ComplaintDTOResponse {
    String postId;
    String userId;
    String firstName;
    String lastName;
    String avatar;
    String complaintType;
    String termOfService;
    LocalDateTime dateTime ;
    boolean reading;
}
