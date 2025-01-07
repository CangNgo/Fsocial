package com.fsocial.profileservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountProfileResponse {
    String id;
    String firstName;
    String lastName;
    String avatar;
}
