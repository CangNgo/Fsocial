package com.fsocial.postservice.dto.profile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    String id;
    String firstName;
    String lastName;
    String avatar;
}
