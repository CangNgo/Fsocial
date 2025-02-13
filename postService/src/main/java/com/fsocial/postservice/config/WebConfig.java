package com.fsocial.postservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Cho phép tất cả các URL
                .allowedOrigins("http://localhost:3000",
                        "http://127.0.0.1:5500") // Nguồn được phép
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Phương thức HTTP được phép
                .allowedHeaders("*") // Header được phép
                .allowCredentials(true); // Cho phép gửi cookie (nếu cần)
    }
}

