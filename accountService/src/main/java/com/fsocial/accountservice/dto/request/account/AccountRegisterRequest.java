package com.fsocial.accountservice.dto.request.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsocial.accountservice.util.RegexdUtils;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountRegisterRequest {
    @Size(min = 6,message = "INVALID_USERNAME")
    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    String username;

    @NotNull(message = "REQUIRED_PASSWORD")
    @NotBlank(message = "REQUIRED_PASSWORD")
    @Size(min = RegexdUtils.PASSWORD_LENGTH, message = "INVALID_PASSWORD")
    @Pattern(regexp = RegexdUtils.PASSWORD_REGEX, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "REQUIRED_EMAIL")
    @NotNull(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    @Pattern(regexp = RegexdUtils.NAME_REGEX, message = "INVALID_NAME")
    String firstName;

    @NotBlank(message = "REQUIRED_USERNAME")
    @NotNull(message = "REQUIRED_USERNAME")
    @Pattern(regexp = RegexdUtils.NAME_REGEX, message = "INVALID_NAME")
    String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    int gender;
}
