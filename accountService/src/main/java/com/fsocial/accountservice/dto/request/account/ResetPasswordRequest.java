package com.fsocial.accountservice.dto.request.account;

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
    String newPassword;
}
