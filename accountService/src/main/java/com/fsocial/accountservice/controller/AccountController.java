package com.fsocial.accountservice.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fsocial.accountservice.config.JwtConfig;
import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.ChangePasswordRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.request.account.EmailRequest;
import com.fsocial.accountservice.dto.request.account.OtpRequest;
import com.fsocial.accountservice.dto.request.account.ResetPasswordRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.AccountStatisticRegiserDTO;
import com.fsocial.accountservice.dto.response.AccountStatisticRegiserLongDateDTO;
import com.fsocial.accountservice.dto.response.auth.DuplicationResponse;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.services.AccountService;
import com.fsocial.accountservice.services.OtpService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Slf4j
public class AccountController {
    AccountService accountServices;
    OtpService otpService;
    HttpServletRequest httpServletRequest;
    JwtConfig jwtConfig;

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;
    private final AccountRepository accountRepository;

    @PostMapping("/register")
    public ApiResponse<Void> persistAccount(@RequestBody @Valid AccountRegisterRequest request) {
        accountServices.persistAccount(request);
        return buildResponse(null, ResponseStatus.ACCOUNT_REGISTERED);
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody @Valid EmailRequest request) {
        otpService.sortTypeForSendOtp(request);
        return buildResponse(null, ResponseStatus.OTP_SENT);
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody @Valid OtpRequest request) {
        otpService.sortTypeForVerifyOtp(request);
        return buildResponse(null, ResponseStatus.OTP_VALID);
    }

    @PostMapping("/check-duplication")
    public ResponseEntity<ApiResponse<DuplicationResponse>> checkDuplication(
            @RequestBody @Valid DuplicationRequest request) {
        ApiResponse<DuplicationResponse> response = accountServices.checkDuplication(request);
        HttpStatus status = response.getStatusCode() != 200 ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        accountServices.resetPassword(request.getEmail(), request.getNewPassword());
        return buildResponse(null, ResponseStatus.PASSWORD_RESET_SUCCESS);
    }

    @GetMapping("/{userId}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable String userId) {
        AccountResponse account = accountServices.getUser(userId);
        return buildResponse(account, ResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        accountServices.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return buildResponse(null, ResponseStatus.PASSWORD_CHANGED);
    }

    private <T> ApiResponse<T> buildResponse(T data, ResponseStatus responseStatus) {
        return ApiResponse.<T>builder()
                .statusCode(responseStatus.getCODE())
                .message(responseStatus.getMessage())
                .dateTime(LocalDateTime.now())
                .data(data)
                .build();
    }

    @GetMapping("/exists")
    public ApiResponse<Map<String, Boolean>> existsAccountByUserId(@RequestParam String userId) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("exists", accountServices.existsById(userId));
        return ApiResponse.<Map<String, Boolean>>builder()
                .data(exists)
                .message("Kiểm tra userId có tồn tại hay không thành công")
                .build();
    }

    @GetMapping("/statistics_register_today")
    public ApiResponse<List<AccountStatisticRegiserDTO>> statisticsRegister(
            @RequestParam("date_time") String dateTime) {
        LocalDate date = LocalDate.parse(dateTime);
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59, 59);

        List<AccountStatisticRegiserDTO> res = accountServices.countByCreatedAtByHours(startDate, endDate);
        return ApiResponse.<List<AccountStatisticRegiserDTO>>builder()
                .data(res)
                .message("Thống kê số lượng tài khoản được tạo trong ngày " + date + " thành công")
                .build();
    }

    @GetMapping("/statistics_register_start_end")
    public ApiResponse<List<AccountStatisticRegiserLongDateDTO>> statisticsRegisterStartEnd(
            @RequestParam("startDate") String startDateRe, @RequestParam("endDate") String endDateRe) {
        LocalDate start = LocalDate.parse(startDateRe);
        LocalDate end = LocalDate.parse(endDateRe);
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(23, 59, 59);

        List<AccountStatisticRegiserLongDateDTO> res = accountServices.countByCreatedAtByStartEnd(startDate, endDate);
        return ApiResponse.<List<AccountStatisticRegiserLongDateDTO>>builder()
                .data(res)
                .message("Lấy danh sách thống kê số lượng tài khoản được tạo từ " + startDate + " đến " + endDate
                        + " thành công")
                .build();
    }

    @PostMapping("/ban")
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Object> banAccount(@RequestParam("user_id") String userId) {
        String banUser = accountServices.banUser(userId);
        // String sub = jwt.getBody().toString();
        return ApiResponse.<Object>builder()
                .data(banUser)
                .message("Ban tài khoản thành công")
                .build();
    }

    public String getScopeFromContext() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(Object::toString)
                .filter(auths -> auths.startsWith("SUB_"))
                .map(auths -> auths.substring(3))
                .findFirst()
                .orElse("No scope found");
    }

}
