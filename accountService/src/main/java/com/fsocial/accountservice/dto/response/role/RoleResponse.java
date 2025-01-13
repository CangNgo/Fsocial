package com.fsocial.accountservice.dto.response.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class RoleResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
