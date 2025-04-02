package com.fsocial.timelineservice.dto.post;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostStatisticsLongDateDTO {
    LocalDateTime date;
    int count;
}
