package com.cangngo.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Hỗ trợ domain local, production và các preview domain của Vercel
        config.setAllowedOriginPatterns(
                List.of(
                        "http://localhost:3000",
                        "https://fsocial-fe.vercel.app",
                        "https://*.vercel.app"
                )
        );
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        // Cho phép tất cả headers để tránh lỗi khi trình duyệt gửi Access-Control-Request-Headers động
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("authorization", "Authorization", "location"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

}
