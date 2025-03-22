package com.fsocial.postservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsocial.postservice.dto.Response;
import com.fsocial.postservice.exception.StatusCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        StatusCode statusCode = StatusCode.UNAUTHENTICATED;

        response.setStatus(statusCode.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response apiResponse = Response.builder()
                .statusCode(statusCode.getCode())
                .message(statusCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.resetBuffer();
    }
}

