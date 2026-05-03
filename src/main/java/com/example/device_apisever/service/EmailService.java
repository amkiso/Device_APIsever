package com.example.device_apisever.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Gửi email bất đồng bộ để không block luồng xử lý chính.
     * Sử dụng MimeMessage để hỗ trợ HTML nội dung.
     */
    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true cho phép gửi HTML

            javaMailSender.send(message);
            System.out.println("Đã gửi email thành công tới: " + to);
            
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email tới " + to + ": " + e.getMessage());
        }
    }

    /**
     * Gửi Email chứa mã OTP
     */
    @Async
    public void sendOtpEmailAsync(String to, String otp) {
        String subject = "Mã xác nhận OTP - Hệ thống Quản lý Thuê Thiết Bị";
        String body = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333; border: 1px solid #ddd; max-width: 500px; margin: 0 auto;'>"
                + "<h2 style='color: #0056b3; text-align: center;'>Xác nhận đăng ký tài khoản</h2>"
                + "<p>Xin chào,</p>"
                + "<p>Bạn đã yêu cầu đăng ký tài khoản hoặc lấy lại mật khẩu. Vui lòng sử dụng mã OTP dưới đây để xác nhận:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "  <span style='font-size: 24px; font-weight: bold; background-color: #f4f4f4; padding: 10px 20px; border-radius: 5px; letter-spacing: 5px;'>" + otp + "</span>"
                + "</div>"
                + "<p style='color: red; font-size: 14px;'>Mã này sẽ hết hạn sau <b>5 phút</b>. Không chia sẻ mã này cho bất kỳ ai.</p>"
                + "<hr>"
                + "<p style='font-size: 12px; color: #888; text-align: center;'>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>"
                + "</div>";

        sendEmailAsync(to, subject, body);
    }
}
