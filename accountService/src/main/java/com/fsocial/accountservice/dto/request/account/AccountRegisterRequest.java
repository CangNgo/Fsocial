package com.fsocial.accountservice.dto.request.account;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsocial.accountservice.validation.constrain.DobValid;
import com.fsocial.accountservice.validation.constrain.NameValid;
import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
import com.fsocial.accountservice.validation.constrain.PasswordValid;

import jakarta.validation.constraints.Email;
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
public class AccountRegisterRequest {
    @Size(min = 6, message = "INVALID_USERNAME")
    @NotNullOrBlank(message = "REQUIRED_USERNAME")
    String username;

    @NotNullOrBlank(message = "REQUIRED_PASSWORD")
    @PasswordValid
    String password;

    @NotNullOrBlank(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotNullOrBlank(message = "REQUIRED_USERNAME")
    @NameValid
    String firstName;

    @NotNullOrBlank(message = "REQUIRED_USERNAME")
    @NameValid
    String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DobValid
    LocalDate dob;

    int gender;
}
