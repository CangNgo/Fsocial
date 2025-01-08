package com.fsocial.accountservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountRequest {
    @Size(min = 6, max = 50,message = "Username ít nhất 6 ký tự")
    @Email
    @NotNull
    String username;

    @Size(min = 6, max = 50,message = "Password ít nhất 6 ký tự")
    @NotNull
    String password;
}
