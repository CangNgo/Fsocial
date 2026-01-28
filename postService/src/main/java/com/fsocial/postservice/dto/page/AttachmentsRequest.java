package com.fsocial.postservice.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AttachmentsRequest {

    @Schema(description = "User id ", defaultValue = "43e76a29-4153-42aa-b692-1bc65fab7f8d")
    String userId;
    @Schema(description = "Attachments page", defaultValue = "1")
    int page;
    @Schema(description = "Attachments page size", defaultValue = "10")
    int pageSize;
}
