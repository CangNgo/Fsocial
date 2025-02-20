package com.fsocial.profileservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindProfileDTO {
    String id;
    String userId;
    String firstName;
    String lastName;
    String avatar;
}
