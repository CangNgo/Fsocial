package com.fsocial.accountservice.dto.request;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
