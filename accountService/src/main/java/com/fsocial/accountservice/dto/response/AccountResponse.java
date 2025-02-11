package com.fsocial.accountservice.dto.response;

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
<<<<<<< HEAD
    RoleResponse role;
=======
    Role role;
    String firstName;
    String lastName;
    String avatar;
>>>>>>> c865938a7cd9ca1c50772e3e22c0b2e435a40bf2
}
