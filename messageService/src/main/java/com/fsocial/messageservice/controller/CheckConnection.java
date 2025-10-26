package com.fsocial.messageservice.controller;

import com.fsocial.messageservice.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class CheckConnection {
    @GetMapping
    public ApiResponse<String> checkConnection() {
        return ApiResponse.<String>builder()
                .message("Connect success")
                .build();
    }
}
