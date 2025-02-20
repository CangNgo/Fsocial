package com.fsocial.accountservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DuplicationCheckResult {
    boolean usernameExists;
    boolean emailExists;
}
