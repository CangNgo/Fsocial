package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.dto.request.account.AccountLoginRequest;
import com.fsocial.accountservice.dto.response.auth.AuthenticationResponse;
import com.fsocial.accountservice.dto.response.auth.IntrospectResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.Token;
import com.fsocial.accountservice.exception.AppCheckedException;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.TokenRepository;
import com.fsocial.accountservice.services.AuthenticationService;
import com.fsocial.accountservice.services.RefreshTokenService;
import com.fsocial.accountservice.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    RefreshTokenService refreshTokenService;
    TokenRepository tokenRepository;

    @Override
    public AuthenticationResponse login(AccountLoginRequest request, String userAgent, HttpServletRequest httpRequest) throws AppCheckedException {

        Account account = accountRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .filter(acc -> acc.getPassword() != null && passwordEncoder.matches(request.getPassword(), acc.getPassword()))
                .orElseThrow(() -> {
                    log.warn("Sai tên tài khoản hoặc mật khẩu: {}", request.getUsername());
                    return new AppException(ErrorCode.LOGIN_FAILED);
                });
        if (!account.isStatus()) throw new AppCheckedException(ErrorCode.ACCOUNT_BANNED);
        String ipAddress = httpRequest.getRemoteAddr();
        String accessToken = jwtService.generateToken(account.getUsername());
//        Lưu token vào db
        Optional<Token> token = tokenRepository.findByAccount(account);
        Token entity;
        if (token.isPresent()) {
             entity = token.get();
            entity.setToken(accessToken);
        }else{
            entity = Token.builder().token(accessToken).build();
        }
        entity.setAccount(account);
        tokenRepository.save(entity);

        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername(), userAgent, ipAddress).getToken();


        log.info("Người dùng {} đăng nhập thành công từ IP: {}", request.getUsername(), ipAddress);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public IntrospectResponse introspect(String token) {
        boolean valid = jwtService.verifyToken(token);
        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }
}
