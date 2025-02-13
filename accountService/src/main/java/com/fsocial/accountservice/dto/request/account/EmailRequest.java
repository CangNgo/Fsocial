package com.fsocial.accountservice.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class EmailRequest {
    @NotBlank(message = "REQUIRED_EMAIL")
    @NotNull(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotBlank(message = "REQUIRED_TYPE_REQUEST")
    @NotNull(message = "REQUIRED_TYPE_REQUEST")
    String type;
}
