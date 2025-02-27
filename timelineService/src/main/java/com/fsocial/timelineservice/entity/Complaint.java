package com.fsocial.timelineservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "complaint")
@Builder
public class Complaint extends  AbstractEntity<String>{
    String postId;
    String userId;
    String complaintType;
    String termOfService;
    LocalDateTime dateTime;
    boolean reading;
}
