package com.fsocial.accountservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    String id;
    String firstName;
    String lastName;
    String avatar;
}
