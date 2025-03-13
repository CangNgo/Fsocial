package com.fsocial.timelineservice.dto.complaint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintStatisticsDTO {
    String hour;
    int count;
}
