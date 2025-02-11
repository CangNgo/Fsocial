package com.fsocial.accountservice.dto.response;

import com.fsocial.accountservice.dto.response.role.RoleResponse;
import com.fsocial.accountservice.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class AccountResponse {
    String id;
    String username;
    boolean isKOL;
    RoleResponse role;
    String firstName;
    String lastName;
    String avatar;
}
