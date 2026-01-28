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

    public void sendOtp(String toEmail, String userName, String otpCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");

//            com.sendgrid.SendGrid sg = new SendGrid(apikey);
            helper.setText(buildOtpEmailContent(htmlOTP, userName, otpCode), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Có lỗi xảy ra khi gửi mail: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private String buildOtpEmailContent(String template, String userName, String otpCode) {
        return template
                .replace("${username}", userName)
                .replace("${otp}", otpCode);
    }

    String htmlOTP = """
            <!DOCTYPE html>
            <html lang="vi">
            
            <head>
                <meta charset="UTF-8">
                <title>Xác minh đăng ký tài khoản</title>
            </head>
            
            <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif;">
                <table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0"
                                style="background-color:#ffffff; border-radius:8px; overflow:hidden;">
            
                                <!-- Header -->
                                <tr>
                                    <td style="background-color:#FF8C00 ; padding:20px; text-align:center;">
                                        <h1 style="color:#ffffff; margin:0; font-size:22px;">
                                            Xác minh đăng ký tài khoản
                                        </h1>
                                    </td>
                                </tr>
            
                                <!-- Body -->
                                <tr>
                                    <td style="padding:30px;">
                                        <p style="font-size:16px; color:#333; margin:0 0 12px;">
                                            Xin chào <strong>${username}</strong>,
                                        </p>
            
                                        <p style="font-size:14px; color:#555; margin:0 0 20px; line-height:1.6;">
                                            Cảm ơn bạn đã đăng ký tài khoản tại <strong>Fsocial</strong>.
                                            Để hoàn tất quá trình đăng ký, vui lòng nhập mã xác minh (OTP) dưới đây:
                                        </p>
                                        <!-- OTP -->
                                        <div style="text-align:center; margin:30px 0;">
                                            <span style="
                                            display:inline-block;
                                            font-size:28px;
                                            letter-spacing:6px;
                                            font-weight:bold;
                                            color:#FFB366;
                                            background-color:#FFF5EB ;
                                            padding:14px 24px;
                                            border-radius:6px;
                                        ">
                                                ${otp}
                                            </span>
                                        </div>
            
                                        <p style="font-size:14px; color:#555; line-height:1.6;">
                                            ⏱ Mã OTP có hiệu lực trong <strong>5 phút</strong>.
                                            Vui lòng không chia sẻ mã này cho bất kỳ ai để đảm bảo an toàn tài khoản.
                                        </p>
            
                                        <p style="font-size:14px; color:#555; margin-top:20px;">
                                            Nếu bạn không thực hiện đăng ký tài khoản, hãy bỏ qua email này.
                                        </p>
                                    </td>
                                </tr>
            
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color:#f8fafc; padding:16px; text-align:center;">
                                        <p style="font-size:12px; color:#94a3b8; margin:0;">
                                            © 2025 Your Company. All rights reserved.
                                        </p>
                                    </td>
                                </tr>
            
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            
            </html>
            """;
}
