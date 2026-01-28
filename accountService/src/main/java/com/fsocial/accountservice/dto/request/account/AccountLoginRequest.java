package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
import com.fsocial.accountservice.validation.constrain.PasswordValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountLoginRequest {
    @Schema(description = "Tên đăng nhập hoặc mật khẩu", defaultValue = "CangNgo")
    @NotNullOrBlank(message = "REQUIRED_USERNAME")
    @Size(min = 6, message = "INVALID_USERNAME")
    String username;

    @Schema(description = "Mật khẩu", defaultValue = "Cang123123")
    @NotNullOrBlank(message = "REQUIRED_PASSWORD")
    @PasswordValid
    String password;
}
