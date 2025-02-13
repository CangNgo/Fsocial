package com.fsocial.accountservice.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ResetPasswordRequest {
    String otp;
    String email;

    @NotNull
    @NotBlank
    String newPassword;
}
