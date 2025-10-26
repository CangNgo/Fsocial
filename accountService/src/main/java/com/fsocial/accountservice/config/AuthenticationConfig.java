package com.fsocial.accountservice.config;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("Authorization");

        log.info("Token: {}", token);

        if (StringUtils.hasText(token))
            requestTemplate.header("Authorization", token);
    }
}
