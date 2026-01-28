package com.fsocial.postservice.dto.relationships;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class FollowRequest {

    @NotBlank(message = "REQUIRED_FIELD")
    String userId;

    @NotBlank(message = "REQUIRED_FIELD")
    String targetId;
}
