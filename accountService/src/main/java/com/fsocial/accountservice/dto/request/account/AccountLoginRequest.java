package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.util.PasswordUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountLoginRequest {
    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    String username;

    @NotNull(message = "REQUIRED_PASSWORD")
    @NotBlank(message = "REQUIRED_PASSWORD")
    @Size(min = PasswordUtils.PASSWORD_LENGTH, message = "INVALID_PASSWORD")
    @Pattern(regexp = PasswordUtils.PASSWORD_REGEX,
            message = "INVALID_PASSWORD")
    String password;
}
