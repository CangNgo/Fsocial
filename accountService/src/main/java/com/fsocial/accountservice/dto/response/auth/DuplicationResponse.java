package com.fsocial.accountservice.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuplicationResponse {
    String username;
    String email;
}
