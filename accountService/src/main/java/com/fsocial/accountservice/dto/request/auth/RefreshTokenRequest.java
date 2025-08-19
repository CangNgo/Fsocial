package com.fsocial.accountservice.dto.request.auth;

import com.fsocial.accountservice.validation.constrain.NotNullOrBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    @NotNullOrBlank(message = "REQUIRED_TOKEN")
    String refreshToken;
}
