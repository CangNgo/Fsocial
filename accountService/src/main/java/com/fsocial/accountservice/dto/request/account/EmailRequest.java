package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
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

    @NotNullOrBlank(message = "REQUIRED_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @NotNullOrBlank(message = "REQUIRED_TYPE_REQUEST")
    String type;
}
