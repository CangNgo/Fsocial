package com.fsocial.notificationService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileNameResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String avatar;
}
