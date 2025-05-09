package com.cangngo.apigateway.config;

import com.cangngo.apigateway.dto.ApiResponse;
import com.cangngo.apigateway.enums.ErrorCode;
import com.cangngo.apigateway.service.AccountService;
import com.cangngo.apigateway.service.BanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GlobalConfig implements GlobalFilter, Ordered {
    AccountService accountService;
    ObjectMapper objectMapper;
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    BanService banService;
    RedisTemplate redisTemplate;
    @NonFinal
    private String[] PUBLIC_ENDPOINT = {"/account/**"
            ,"/post/**","/timeline/**","/profile/**",
    };

    @NonFinal
    @Value("${app.api-prefix}")
    String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeaders))
            return unauthenticated(exchange.getResponse());

        String tokenrequest = authHeaders.getFirst();
//        System.out.println("Token: " + tokenrequest);
        boolean isBan = banService.isBan(tokenrequest.substring(7));
        if (isBan){
            return banned(exchange.getResponse());
        }else{
            System.out.println("account không bị ban");
        }
        //kiểm tra nếu tồn tại trong backList thì chặn request

//        Set<String> keys = redisTemplate.keys("*");
//
//        for (String key : keys) {
//            Object type = redisTemplate.opsForValue().get(key);
//            System.out.println("Key: " + key + ", Type: " + type);
//        }

        String token = authHeaders.getFirst().replace("Bearer ", "");
        return accountService.apiResponseMono(token).flatMap(introspectResponse -> {
                    if (introspectResponse.getData().isValid())
                        return chain.filter(exchange);
                    else
                        return unauthenticated(exchange.getResponse());
                })
                .onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath().replaceFirst(apiPrefix, "");
        return Arrays.stream(PUBLIC_ENDPOINT).anyMatch(endPoint -> antPathMatcher.match(endPoint, path));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .statusCode(ErrorCode.UNAUTHENTICATED.getCode())
                .message(ErrorCode.UNAUTHENTICATED.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }

    Mono<Void> banned(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .statusCode(ErrorCode.ACCOUNT_BANNED.getCode())
                .message(ErrorCode.ACCOUNT_BANNED.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }

}