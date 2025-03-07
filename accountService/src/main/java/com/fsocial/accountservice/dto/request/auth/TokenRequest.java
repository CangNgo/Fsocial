package com.fsocial.accountservice.dto.request.auth;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class TokenRequest {
    @NotNullOrBlank(message = "REQUIRED_TOKEN")
    String token;
}
