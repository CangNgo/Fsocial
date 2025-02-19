package com.fsocial.postservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fsocial.postservice.exception.StatusCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    int statusCode  = 200;
    String message;
    Object data;
    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime = LocalDateTime.now();

}
