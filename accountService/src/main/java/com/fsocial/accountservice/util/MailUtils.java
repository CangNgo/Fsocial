package com.fsocial.accountservice.util;

import com.fsocial.accountservice.enums.ErrorCode;
import com.fsocial.accountservice.exception.AppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MailUtils {

    @Autowired
    private final JavaMailSender javaMailSender;

    @NonFinal
    @Value("${spring.mail.from}")
    String fromEmail;

    @NonFinal
    @Value("${spring.mail.password}")
    String apikey;

    public void sendOtp(String toEmail, String otpCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            System.out.println("Sending email from " + fromEmail);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");

//            com.sendgrid.SendGrid sg = new SendGrid(apikey);
            helper.setText(buildOtpEmailContent(otpCode), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Có lỗi xảy ra khi gửi mail: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private String buildOtpEmailContent(String otpCode) {
        return String.format(
                "<h1>Your OTP Code</h1>" +
                        "<p>Please use the following OTP code to proceed: <b>%s</b></p>" +
                        "<p>This code will expire in 5 minutes.</p>", otpCode);
    }
}
