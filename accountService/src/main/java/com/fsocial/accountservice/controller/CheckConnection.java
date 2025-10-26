package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
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
