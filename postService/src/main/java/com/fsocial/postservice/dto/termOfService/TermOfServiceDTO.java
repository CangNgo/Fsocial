package com.fsocial.postservice.dto.termOfService;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TermOfServiceDTO {
    String id;
    String name;
}
