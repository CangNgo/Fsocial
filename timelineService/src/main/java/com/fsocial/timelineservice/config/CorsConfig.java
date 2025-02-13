package com.fsocial.timelineservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả API
                        .allowedOrigins("http://localhost:3000") // Chỉ cho phép React truy cập
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức cho phép
                        .allowedHeaders("*") // Chấp nhận tất cả headers
                        .allowCredentials(true); // Hỗ trợ gửi cookie nếu cần
            }
        };
    }
}