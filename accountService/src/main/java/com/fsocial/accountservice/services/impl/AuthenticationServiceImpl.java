package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.request.auth.TokenRequest;
import com.fsocial.accountservice.dto.response.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.IntrospectResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.services.AuthenticationService;
import com.fsocial.accountservice.services.RefreshTokenService;
import com.fsocial.accountservice.services.JwtService;
import com.nimbusds.jose.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    JwtService tokenService;
    RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse login(AccountLoginRequest request, String userAgent, HttpServletRequest httpRequest) {
        Account account = accountRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .filter(acc -> acc.getPassword() != null && passwordEncoder.matches(request.getPassword(), acc.getPassword()))
                .orElseThrow(() -> {
                    log.warn("Sai tên tài khoản hoặc mật khẩu: {}", request.getUsername());
                    return new AppException(ErrorCode.LOGIN_FAILED);
                });

        String ipAddress = httpRequest.getRemoteAddr();
        String accessToken = tokenService.generateToken(account.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername(), userAgent, ipAddress).getToken();

        log.info("Người dùng {} đăng nhập thành công từ IP: {}", request.getUsername(), ipAddress);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public IntrospectResponse introspectValid(TokenRequest request) throws ParseException, JOSEException {
        tokenService.verifyToken(request.getToken());
        return IntrospectResponse.builder()
                    .valid(true)
                    .build();
    }

    @Override
    public void logout(TokenRequest request) {
        if (request == null) throw new AppException(ErrorCode.UNAUTHENTICATED);
        tokenService.disableToken(request.getToken());
    }
}
