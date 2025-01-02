package com.fsocial.accountservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountDTO {
    @Size(min = 6, max = 50,message = "Username ít nhất 5 ký tự")
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;
}
