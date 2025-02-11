package com.fsocial.accountservice.dto.request.account;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OtpRequest {
    String email;
    String otp;
    String type;
}
