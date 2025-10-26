package com.fsocial.accountservice.dto.request.account;

import com.fsocial.accountservice.validation.constrain.PasswordValid;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChangePasswordRequest {
    @PasswordValid
    String oldPassword;

    @PasswordValid
    String newPassword;
}
