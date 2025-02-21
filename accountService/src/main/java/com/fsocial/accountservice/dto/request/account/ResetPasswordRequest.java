package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.util.PasswordUtils;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ResetPasswordRequest {
    @NotBlank(message = "REQUIRED_OTP")
    @NotNull(message = "REQUIRED_OTP")
    String otp;

    @NotBlank(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotNull(message = "REQUIRED_PASSWORD")
    @NotBlank(message = "REQUIRED_PASSWORD")
    @Size(min = PasswordUtils.PASSWORD_LENGTH, message = "INVALID_PASSWORD")
    @Pattern(regexp = PasswordUtils.PASSWORD_REGEX,
            message = "INVALID_PASSWORD")
    String newPassword;
}
