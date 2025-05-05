package com.fsocial.accountservice.services.impl;

import com.fsocial.accountservice.config.JwtConfig;
import com.fsocial.accountservice.dto.ApiResponse;
import com.fsocial.accountservice.dto.request.account.AccountRegisterRequest;
import com.fsocial.accountservice.dto.request.account.DuplicationRequest;
import com.fsocial.accountservice.dto.response.AccountResponse;
import com.fsocial.accountservice.dto.response.AccountStatisticRegiserDTO;
import com.fsocial.accountservice.dto.response.AccountStatisticRegiserLongDateDTO;
import com.fsocial.accountservice.dto.response.auth.DuplicationResponse;
import com.fsocial.accountservice.entity.Account;
import com.fsocial.accountservice.entity.RefreshToken;
import com.fsocial.accountservice.entity.Role;
import com.fsocial.accountservice.entity.Token;
import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.enums.RedisKeyType;
import com.fsocial.accountservice.enums.ResponseStatus;
import com.fsocial.accountservice.exception.AppException;
import com.fsocial.accountservice.mapper.AccountMapper;
import com.fsocial.accountservice.mapper.ProfileMapper;
import com.fsocial.accountservice.repository.AccountRepository;
import com.fsocial.accountservice.repository.RefreshTokenRepository;
import com.fsocial.accountservice.repository.RoleRepository;
import com.fsocial.accountservice.repository.TokenRepository;
import com.fsocial.accountservice.repository.httpclient.ProfileClient;
import com.fsocial.accountservice.services.AccountService;
import com.fsocial.accountservice.services.BanService;
import com.fsocial.accountservice.services.OtpService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    AccountMapper accountMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    OtpService otpService;
    BanService banService;
    TokenRepository tokenRepository;
    RefreshTokenRepository refreshTokenRepository;
    HttpServletRequest httpServletRequest;
    JwtConfig jwtConfig;

    static String DEFAULT_ROLE = "USER";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void persistAccount(AccountRegisterRequest request) {
        validateAccountExistence(request.getUsername(), request.getEmail());
        Account account = saveAccount(request);
        createProfile(account, request);
        otpService.deleteOtp(request.getEmail(), RedisKeyType.REGISTER.getRedisKeyPrefix());
    }

    @Override
    public AccountResponse getUser(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(account.getId());
        accountRepository.save(account);
        log.info("Đặt lại mật khẩu thành công.");
    }

    @Override
    public ApiResponse<DuplicationResponse> checkDuplication(DuplicationRequest request) {
        boolean usernameExisted = accountRepository.countByUsername(request.getUsername()) > 0;
        boolean emailExisted = accountRepository.countByEmail(request.getEmail()) > 0;

        DuplicationResponse response = DuplicationResponse.builder()
                .username(usernameExisted ? ErrorCode.USERNAME_EXISTED.getMessage() : null)
                .email(emailExisted ? ErrorCode.EMAIL_EXISTED.getMessage() : null)
                .build();

        boolean hasError = usernameExisted || emailExisted;

        return ApiResponse.<DuplicationResponse>builder()
                .statusCode(hasError ? ErrorCode.DUPLICATION.getCode() : ResponseStatus.VALID.getCODE())
                .message(hasError ? ErrorCode.DUPLICATION.getMessage() : ResponseStatus.VALID.getMessage())
                .data(hasError ? response : null)
                .build();
    }

    @Override
    public void changePassword(String userId, String odlPassword, String newPassword) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (!passwordEncoder.matches(odlPassword, account.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(account.getId());
        accountRepository.save(account);
        log.info("Đổi mật khẩu thành công.");
    }

    @Override
    public boolean existsById(String id) {
        return accountRepository.findById(id).isPresent();
    }

    @Override
    public List<AccountStatisticRegiserDTO> countByCreatedAtByHours(LocalDateTime startDay, LocalDateTime endDay) {

        List<Object[]> results = accountRepository.countByCreatedAtByHours(startDay, endDay);

        List<AccountStatisticRegiserDTO> res = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();

        // Lưu dữ liệu vào map theo dạng <hour, count>
        for (Object[] row : results) {
            int hour = ((Number) row[0]).intValue();  // Chuyển Object thành int
            int count = ((Number) row[1]).intValue(); // Chuyển Object thành int
            map.put(hour, count);
        }

        // Duyệt từ 00:00 - 23:00, kiểm tra map để lấy giá trị
        for (int hour = 0; hour < 24; hour++) {
            String formattedHour = String.format("%02d:00", hour); // Định dạng HH:00
            res.add(new AccountStatisticRegiserDTO(formattedHour, map.getOrDefault(hour, 0)));
        }
        return res;
    }

    @Override
    public List<AccountStatisticRegiserLongDateDTO> countByCreatedAtByStartEnd(LocalDateTime startDay, LocalDateTime endDay) {
        List<Object[]> repo = accountRepository.countByCreatedAtByDate(startDay, endDay);
        List<AccountStatisticRegiserLongDateDTO> res = new ArrayList<>();
        // Chuyển đổi dữ liệu từ repo thành Map để dễ dàng truy cập
        Map<Date, Long> dateCountMap = new HashMap<>();
        for (Object[] dto : repo) {
            Date date = (Date) dto[0];
            Long count = ((Number) dto[1]).longValue(); // Đảm bảo chuyển đổi số đúng cách
            dateCountMap.put(date, count);
        }

// Tạo Date từ LocalDateTime
        Date start = convertToDateViaInstant(startDay);
        Date end = convertToDateViaInstant(endDay);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

// Vòng lặp qua từng ngày
        while (!calendar.getTime().after(end)) {
            Date currentDate = calendar.getTime();

            // Sử dụng chỉ phần ngày (không bao gồm giờ, phút, giây)
            Date dateOnly = removeTime(currentDate);

            // Kiểm tra xem ngày này có trong dữ liệu không
            if (dateCountMap.containsKey(dateOnly)) {
                res.add(new AccountStatisticRegiserLongDateDTO(dateOnly, dateCountMap.get(dateOnly)));
            } else {
                res.add(new AccountStatisticRegiserLongDateDTO(dateOnly, 0L));
            }

            // Tăng ngày lên 1
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return res;
    }

    // Phương thức để loại bỏ thông tin thời gian, chỉ giữ lại ngày
    private Date removeTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    @Transactional
    public String banUser(String userId) {

        //lấy thông tin từ token

        Account banAccount = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        Optional<Token> tokenAccount = tokenRepository.findByAccount(banAccount);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsernameAndExpiryDate_Max(banAccount.getUsername());
        //ban account
        banAccount.setStatus(false);
        accountRepository.save(banAccount);
        //ban token
        tokenAccount.ifPresent(token -> banService.ban(token.getToken()));
        refreshToken.ifPresent(refresh -> refreshTokenRepository.deleteByToken(refresh.getToken()));
        return "Ban account: " + banAccount.getUsername() + " successfull";
    }

    private void validateAccountExistence(String username, String email) {
        boolean accountExisted = accountRepository.countByUsernameOrEmail(username, email) > 0;
        if (accountExisted) throw new AppException(ErrorCode.ACCOUNT_EXISTED);

        otpService.validEmailBeforePersist(email);
    }

    private Account saveAccount(AccountRegisterRequest request) {
        Account account = accountMapper.toEntity(request);
        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(getDefaultRole());
        return accountRepository.save(account);
    }

    private void createProfile(Account account, AccountRegisterRequest request) {
        var profileRequest = profileMapper.toProfileRegister(request);
        profileRequest.setUserId(account.getId());
        profileClient.createProfile(profileRequest);
    }

    private Role getDefaultRole() {
        return roleRepository.findById(DEFAULT_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getUserIdFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getSignerKey().getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public static Date convertToDateViaInstant(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
