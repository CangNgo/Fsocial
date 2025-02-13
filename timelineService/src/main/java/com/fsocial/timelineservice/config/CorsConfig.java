<<<<<<<< HEAD:profileService/src/main/java/com/fsocial/profileservice/config/WebConfig.java
package com.fsocial.profileservice.config;
========
package com.fsocial.timelineservice.config;
>>>>>>>> rollback2:timelineService/src/main/java/com/fsocial/timelineservice/config/CorsConfig.java

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // Thay đổi từ allowedOrigins sang allowedOriginPatterns
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}