package com.fsocial.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fsocial.profileservice.enums.ResponseStatus;
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
    @Builder.Default
    int statusCode = 200;
    String message;
    T data;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime = LocalDateTime.now();

    public static <T> ApiResponse<T> buildApiResponse(T data, ResponseStatus status) {
        return ApiResponse.<T>builder()
                .statusCode(status.getCODE())
                .message(status.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }
}
