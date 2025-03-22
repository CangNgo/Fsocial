package com.fsocial.timelineservice.dto.complaint;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintDTOResponse {
    String id;
    String postId;
    String userId;
    String firstName;
    String profileId;
    String lastName;
    String complaintType;
    String termOfService;
    LocalDateTime dateTime ;
    boolean reading;
}
