package com.fsocial.timelineservice.dto.post;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostStatisticsDTO {
        String hour;
        int count;
}
