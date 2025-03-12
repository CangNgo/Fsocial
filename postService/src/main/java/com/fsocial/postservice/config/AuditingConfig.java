package com.fsocial.postservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditingConfig {
    @Bean
    public ApplicationAuditAware auditorAware (){
        return new ApplicationAuditAware();
    }
}
