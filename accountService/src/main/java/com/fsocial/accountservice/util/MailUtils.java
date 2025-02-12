package com.fsocial.accountservice.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MailUtils {

    JavaMailSender javaMailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String toEmail, String otpCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");
            helper.setText(buildOtpEmailContent(otpCode), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private String buildOtpEmailContent(String otpCode) {
        return String.format(
                "<h1>Your OTP Code</h1>" +
                        "<p>Please use the following OTP code to proceed: <b>%s</b></p>" +
                        "<p>This code will expire in 5 minutes.</p>", otpCode);
    }
}
