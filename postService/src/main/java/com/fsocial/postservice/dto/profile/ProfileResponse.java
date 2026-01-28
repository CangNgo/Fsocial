package com.fsocial.postservice.dto.profile;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProfileResponse {
    String id;
    String firstName;
    String lastName;
    String avatar;
}

