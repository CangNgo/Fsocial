package com.cangngo.apigateway.service.impl;

import com.cangngo.apigateway.dto.ApiResponse;
import com.cangngo.apigateway.dto.request.TokenRequest;
import com.cangngo.apigateway.dto.response.IntrospectResponse;
import com.cangngo.apigateway.repository.AccountClient;
import com.cangngo.apigateway.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountServiceImpl implements AccountService {
    AccountClient accountClient;

    @Override
    public Mono<ApiResponse<IntrospectResponse>> apiResponseMono(String token) {
        return accountClient.introspect(TokenRequest.builder().token(token).build());
    }
}
