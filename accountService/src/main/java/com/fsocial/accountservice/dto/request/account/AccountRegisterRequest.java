package com.fsocial.accountservice.dto.request.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsocial.accountservice.util.PasswordUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountRegisterRequest {
    @Size(min = 6, max = 50,message = "Username ít nhất 6 ký tự")
    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    String username;

    @NotNull(message = "REQUIRED_PASSWORD")
    @NotBlank(message = "REQUIRED_PASSWORD")
    @Size(min = PasswordUtils.PASSWORD_LENGTH, message = "INVALID_PASSWORD")
    @Pattern(regexp = PasswordUtils.PASSWORD_REGEX,
            message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "REQUIRED_EMAIL")
    @NotNull(message = "REQUIRED_EMAIL")
    String email;

    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    String firstName;

    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    int gender;
}
