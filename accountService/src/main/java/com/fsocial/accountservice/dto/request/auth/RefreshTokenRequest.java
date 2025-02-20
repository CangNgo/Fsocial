package com.fsocial.accountservice.dto.request.auth;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    @NotNullOrBlank(message = "REQUIRED_TOKEN")
    String refreshToken;
}
