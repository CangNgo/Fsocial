package com.fsocial.profileservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountProfileRequest {
    String firstName;
    String lastName;
    String bio;
    String avatar;
    int gender;
    String address;
    LocalDate dob;
}
