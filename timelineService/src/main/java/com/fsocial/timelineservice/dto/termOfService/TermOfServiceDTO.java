package com.fsocial.timelineservice.dto.termOfService;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TermOfServiceDTO {
    String id;
    String name;
    boolean status;
}
