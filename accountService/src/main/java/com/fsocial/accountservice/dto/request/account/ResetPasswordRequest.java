package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
import com.fsocial.accountservice.validation.constrain.PasswordValid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ResetPasswordRequest {

    @NotNullOrBlank(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotNullOrBlank(message = "REQUIRED_PASSWORD")
    @PasswordValid
    String newPassword;
}
