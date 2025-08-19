package com.fsocial.notificationService.config;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class AuthenticationConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("Authorization");

        if (StringUtils.hasText(token))
            requestTemplate.header("Authorization", token);
    }
}
