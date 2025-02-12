package com.fsocial.timelineservice.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileServiceResponse {
    private int statusCode;
    private String message;
    private ProfileResponse data;
    private LocalDateTime dateTime;
}
