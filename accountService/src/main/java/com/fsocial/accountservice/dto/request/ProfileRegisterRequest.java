package com.fsocial.accountservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileRegisterRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    int gender;
}
