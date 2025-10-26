package com.fsocial.profileservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilePageResponse {
    String firstName;
    String lastName;
    String avatar;
    String banner;
    String bio;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    List<UserResponse> followers;
}
