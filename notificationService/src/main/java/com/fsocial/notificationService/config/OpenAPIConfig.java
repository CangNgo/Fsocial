package com.fsocial.notificationService.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8087}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        // Gateway server - for production use
        Server gatewayServer = new Server()
                .url("http://localhost:8888/api/v1/notification")
                .description("API Gateway Server");
        // Direct service server - for development
        Server localServer = new Server()
                .url("http://localhost:" + serverPort + "/notification")
                .description("Local Development Server");

        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service API")
                        .version("1.0")
                        .description("API documentation for Notification Service - Quản lý thông báo và push notification")
                        .license(new License()
                                .name("API License")
                                .url("https://www.fsocial.com")))
                .servers(List.of(gatewayServer, localServer))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token authentication")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }
}

