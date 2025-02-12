package com.fsocial.timelineservice.dto.profile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String id;
    String firstName;
    String lastName;
    String avatar;
}
