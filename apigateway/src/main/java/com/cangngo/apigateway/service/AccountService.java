package com.cangngo.apigateway.service;

import com.cangngo.apigateway.dto.ApiResponse;
import com.cangngo.apigateway.dto.response.IntrospectResponse;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<ApiResponse<IntrospectResponse>> apiResponseMono(String token);
}
