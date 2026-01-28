package com.fsocial.accountservice.controller;

import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Slf4j
public class GoogleAuthen {

    @GetMapping("/google")
    public ApiResponse<Object> loginWithGoogle(OAuth2AuthenticationToken token){
        return ApiResponse.builder().statusCode(200).message("Thông tin user").data(token).build();
    }
}
