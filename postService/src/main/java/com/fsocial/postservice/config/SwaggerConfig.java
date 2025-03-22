package com.fsocial.postservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig{

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.service.title}")String title,
            @Value("${openapi.service.server}")String server,
            @Value("${openapi.service.description}")String description,
            @Value("${openapi.service.version}")String version,
            @Value("${openapi.service.serverName}")String serverName) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .license(new License().name("API License").url("http:domain/cangngo.vn"))
                )
                .servers(List.of(new Server().url(server).description(serverName), new Server().url("http://localhost:8084").description("timeline service")))
                .components(new Components().addSecuritySchemes(
                        "brearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("brearerAuth")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("postservice")
                .packagesToScan("com.fsocial.postservice.controller")
                .build();
    }
}
