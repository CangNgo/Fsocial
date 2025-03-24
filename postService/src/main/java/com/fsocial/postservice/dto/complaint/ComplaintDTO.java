package com.fsocial.postservice.dto.complaint;

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
    @NotBlank(message = "postId không được để trống")
    String postId;
    @NotBlank(message = "userId không được để trống")
    String userId;
    @NotBlank(message = "complaintType không được để trống")
    String complaintType;
    @NotBlank(message = "termOfServiceId không được để trống")
    String termOfServiceId;
    LocalDateTime createDatetime = LocalDateTime.now();
    boolean readding;
}
