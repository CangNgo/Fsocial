package com.fsocial.accountservice.dto.request.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PageRequest {
    @Schema(description = "Attachments page", defaultValue = "1")
    int page;
    @Schema(description = "Attachments page size", defaultValue = "10")
    int pageSize;
}
