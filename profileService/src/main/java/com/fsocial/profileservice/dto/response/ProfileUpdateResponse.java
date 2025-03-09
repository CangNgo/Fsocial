package com.fsocial.profileservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileUpdateResponse {
    String firstName;
    String lastName;
    LocalDate dob;
    String bio;
    String avatar;
    String banner;
    String address;
    LocalDate createdAt;
    LocalDate updatedAt;
}
