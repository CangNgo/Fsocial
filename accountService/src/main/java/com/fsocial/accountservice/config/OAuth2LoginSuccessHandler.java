package com.fsocial.accountservice.config;

import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.services.AccountService;
import com.fsocial.accountservice.services.AuthenticationService;
import com.fsocial.accountservice.services.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final AccountService accountService;
    private final AuthenticationService authenticationService;
    String format = "%s/oauth2/callback?token=%s&username=%s&refreshToken=%s";
    @Value("${react.frontend}")
    private String FRONTEND_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2User: " + oAuth2User);
        // Lấy thông tin user từ Google
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String lastName = (String) attributes.get("family_name");
        String firstName = (String) attributes.get("given_name");

        String googleId = (String) attributes.get("sub");
        System.out.println("attributes: , " + attributes);
        try {
            Account account;
            Optional<Account> existingAccount = accountService.findByEmail(email);

            if (existingAccount.isPresent()) {
                account = existingAccount.get();
            } else {
                String password = genericPassword((String) attributes.get("name"));

                AccountRegisterRequest accountRegisterRequest =
                        AccountRegisterRequest.builder()
                                .username(email)
                                .password(password)
                                .email(email)
                                .firstName(firstName)
                                .lastName(lastName)
                                .build();
                account = accountService.registerGoogleAccount(accountRegisterRequest, googleId);
            }

            // Generate token using AuthenticationService (reusing login logic)
            // Pass userAgent and request for refresh token generation
            String userAgent = request.getHeader("User-Agent");
            AuthenticationResponse authResponse = authenticationService.loginGoogle(account, userAgent, request);

            // Redirect về React app với token
            String redirectUrl = String.format(
                    format,
                    FRONTEND_URL,
                    authResponse.getAccessToken(),
                    account.getUsername(),
                    authResponse.getRefreshToken());

            log.info("Redirecting to: {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("Error processing Google login: {}", e.getMessage(), e);
            // Redirect về trang login với error
            String errorUrl = String.format("%s/login?error=processing_failed", FRONTEND_URL);
            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }

    public String genericPassword(String name) {

        if (name.length() < 9) {
            return genericPassword(name + name);
        } else if (name.length() > 50) {
            return name.substring(0, 20);
        } else {
            return name;
        }
    }
}