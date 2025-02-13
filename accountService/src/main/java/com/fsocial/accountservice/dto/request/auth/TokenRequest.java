package com.fsocial.accountservice.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class TokenRequest {
    @NotBlank(message = "REQUIRED_TOKEN")
    @NotNull(message = "REQUIRED_TOKEN")
    String token;
}
