package com.fsocial.accountservice.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class DuplicationRequest {
    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    @Size(min = 6, message = "INVALID_USERNAME")
    String username;

    @NotBlank(message = "REQUIRED_EMAIL")
    @NotNull(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;
}
