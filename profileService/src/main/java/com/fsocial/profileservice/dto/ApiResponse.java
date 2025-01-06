package com.fsocial.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>{
    int statusCode;
    String message;
    T data;
    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime = LocalDateTime.now();
}
