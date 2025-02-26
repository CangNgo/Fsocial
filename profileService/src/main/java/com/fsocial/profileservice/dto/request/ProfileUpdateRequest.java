package com.fsocial.profileservice.dto.request;

import com.fsocial.profileservice.validation.constrain.NotNullOrBlank;
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
public class ProfileUpdateRequest {
    @NotNullOrBlank(message = "REQUIRED_FIELDS")
    String firstName;

    @NotNullOrBlank(message = "REQUIRED_FIELDS")
    String lastName;

    LocalDate dob;
    String bio;
    String avatar;
    String address;
    LocalDate updatedAt = LocalDate.now();
}
