package com.fsocial.timelineservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:3000"); // Cho phép React app
                    config.addAllowedMethod("*"); // Cho phép tất cả các method
                    config.addAllowedHeader("*"); // Cho phép tất cả các header
                    config.setAllowCredentials(true); // Cho phép gửi cookie, token xác thực
                    return config;
                }))
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF cho API
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Cho phép tất cả các request
                .build();
    }
}

