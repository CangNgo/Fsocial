package com.fsocial.accountservice.dto.request.account;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    String email;
    String firstName;
    String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    int gender;
}
