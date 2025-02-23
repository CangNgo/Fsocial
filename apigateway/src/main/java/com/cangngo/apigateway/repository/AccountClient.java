package com.cangngo.apigateway.repository;

import com.cangngo.apigateway.dto.ApiResponse;
import com.cangngo.apigateway.dto.request.TokenRequest;
import com.cangngo.apigateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AccountClient {
    @PostExchange(url = "/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody TokenRequest request);
}
