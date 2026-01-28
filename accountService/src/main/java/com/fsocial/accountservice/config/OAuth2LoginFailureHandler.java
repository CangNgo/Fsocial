package com.fsocial.accountservice.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${react.frontend}")
    private String FRONTEND_URL;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                       HttpServletResponse response,
                                       AuthenticationException exception) throws IOException, ServletException {
        
        log.error("=== OAuth2 Login Failure ===");
        log.error("Request URI: {}", request.getRequestURI());
        log.error("Query String: {}", request.getQueryString());
        log.error("Request Method: {}", request.getMethod());
        log.error("Request Parameters:");
        request.getParameterMap().forEach((key, values) -> 
            log.error("  {} = {}", key, String.join(", ", values))
        );
        log.error("Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
            log.error("  {} = {}", headerName, request.getHeader(headerName))
        );
        log.error("Exception Type: {}", exception.getClass().getSimpleName());
        log.error("Exception Message: {}", exception.getMessage());
        
        if (exception instanceof OAuth2AuthenticationException oauth2Exception) {
            OAuth2Error oauth2Error = oauth2Exception.getError();
            log.error("OAuth2 Error Code: {}", oauth2Error.getErrorCode());
            log.error("OAuth2 Error Description: {}", oauth2Error.getDescription());
            log.error("OAuth2 Error URI: {}", oauth2Error.getUri());
        }
        
        log.error("Full Stack Trace:", exception);
        
        // Redirect về frontend với error
        String errorUrl = String.format("%s/login?error=true", FRONTEND_URL);
        getRedirectStrategy().sendRedirect(request, response, errorUrl);
    }
}

