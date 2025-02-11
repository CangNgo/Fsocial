package com.fsocial.accountservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ProfileRegisterResponse {
    String firstName;
    String lastName;
    String avatar;
}
