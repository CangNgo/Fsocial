package com.fsocial.profileservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileAdminResponse {
    String id;
    String firstName;
    String lastName;
    LocalDate dob;
    int gender;
    String avatar;
    String address;
    
}
