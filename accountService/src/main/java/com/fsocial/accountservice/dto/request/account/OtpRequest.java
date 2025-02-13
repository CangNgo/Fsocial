package com.fsocial.accountservice.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OtpRequest {
    @NotBlank(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotBlank(message = "REQUIRED_OTP")
    @NotNull(message = "REQUIRED_OTP")
    String otp;

    @NotBlank(message = "REQUIRED_TYPE_REQUEST")
    @NotNull(message = "REQUIRED_TYPE_REQUEST")
    String type;
}
