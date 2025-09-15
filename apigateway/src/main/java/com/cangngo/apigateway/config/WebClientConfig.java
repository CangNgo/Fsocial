package com.cangngo.apigateway.config;

import com.cangngo.apigateway.repository.AccountClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Value("${ACCOUNT_SERVICE_URL:http://fsocial-accountservice:8081}")
    private String accountServiceUrl;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(accountServiceUrl + "/account")
                .build();
    }

    @Bean
    AccountClient accountClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(AccountClient.class);
    }
}
