package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class DuplicationRequest {
    @NotNullOrBlank(message = "REQUIRED_USERNAME")
    @Size(min = 6, message = "INVALID_USERNAME")
    String username;

    @NotNullOrBlank(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;
}
