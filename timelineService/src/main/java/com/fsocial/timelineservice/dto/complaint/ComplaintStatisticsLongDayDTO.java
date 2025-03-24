package com.fsocial.timelineservice.dto.complaint;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintStatisticsLongDayDTO {
    String date;
    int count;
}
