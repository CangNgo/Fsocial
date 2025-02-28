package com.fsocial.relationshipService.dto.request;

import com.fsocial.relationshipService.validation.constrain.NotNullOrBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class FollowRequest {

    @NotNullOrBlank(message = "REQUIRED_FIELD")
    String userId;

    @NotNullOrBlank(message = "REQUIRED_FIELD")
    String targetId;
}
