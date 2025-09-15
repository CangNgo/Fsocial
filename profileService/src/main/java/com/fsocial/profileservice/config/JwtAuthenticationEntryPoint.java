package com.fsocial.profileservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.enums.ErrorCode;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        List<String> authHeaders = (List<String>) request.getHeaders(HttpHeaders.AUTHORIZATION);

        String tokenrequest = authHeaders.getFirst();
        System.out.println("Token: " + tokenrequest);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .statusCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.resetBuffer();
    }
}

