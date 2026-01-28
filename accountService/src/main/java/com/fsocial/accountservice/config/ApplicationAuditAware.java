package com.fsocial.accountservice.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            return Optional.ofNullable(null);
        }

        Object principal = authentication.getPrincipal();

        // Xử lý OAuth2 Login (Google)
        if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            return Optional.ofNullable(oidcUser.getEmail()); // hoặc oidcUser.getSubject()
        }

        // Xử lý OAuth2 Login khác (không có OIDC)
        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            return Optional.ofNullable(oauth2User.getAttribute("email"));
        }

        // Xử lý JWT Token từ Resource Server
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return Optional.ofNullable(jwt.getSubject());
        }
        // Fallback
        return Optional.ofNullable(authentication.getName());
    }
}