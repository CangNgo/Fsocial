package com.fsocial.accountservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    String username;

    @Size(min = 6, max = 50,message = "Password ít nhất 6 ký tự")
    @NotNull
    String password;

    String mail;
    String firstName;
    String lastName;
    LocalDate dob;
    int gender;
}
