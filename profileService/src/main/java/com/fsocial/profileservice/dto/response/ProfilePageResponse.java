package com.fsocial.profileservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    List<UserResponse> followers;
}
