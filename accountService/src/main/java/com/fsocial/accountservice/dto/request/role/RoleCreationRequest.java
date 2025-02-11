package com.fsocial.accountservice.dto.request.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class RoleCreationRequest {
    String name;
    String description;
    Set<String> permissions;
}
