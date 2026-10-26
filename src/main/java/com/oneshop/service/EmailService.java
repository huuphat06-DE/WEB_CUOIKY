package com.oneshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 * THÀNH VIÊN 1: Dịch vụ Gửi Email xác nhận mã OTP thật qua Google SMTP Server
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * Sinh mã OTP 6 chữ số ngẫu nhiên
     */
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Gửi email chứa mã OTP xác nhận tài khoản trực tiếp qua Google SMTP tới BẤT KỲ GMAIL NÀO
     * @return true nếu gửi thành công qua Google SMTP, false nếu gặp sự cố mạng/cấu hình (vẫn in OTP ra Console)
     */
    public boolean sendOtpEmail(String toEmail, String otpCode) {
        System.out.println("==================================================================");
        System.out.println(">>> ĐANG KẾT NỐI GOOGLE SMTP ĐỂ GỬI OTP TỚI: " + toEmail);
        System.out.println(">>> MÃ OTP TẠO RA: [ " + otpCode + " ] (Thời hạn 5 phút)");
        System.out.println("==================================================================");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, "SkinViet_MyPham Shop");
            helper.setTo(toEmail);
            helper.setSubject("[SkinViet] Mã OTP xác thực đăng ký tài khoản");

            String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 550px; margin: 0 auto; padding: 25px; border: 1px solid #f1e4e8; border-radius: 12px; background-color: #ffffff;\">"
                    + "<div style=\"text-align: center; margin-bottom: 20px;\">"
                    + "<h2 style=\"color: #d84374; margin: 0;\">SkinViet_MyPham Shop</h2>"
                    + "<p style=\"color: #6c757d; font-size: 14px; margin-top: 5px;\">Chuỗi bán lẻ mỹ phẩm chính hãng hàng đầu</p>"
                    + "</div>"
                    + "<hr style=\"border: none; border-top: 1px solid #f1e4e8; margin-bottom: 20px;\">"
                    + "<p style=\"font-size: 15px; color: #333333;\">Xin chào quý khách,</p>"
                    + "<p style=\"font-size: 15px; color: #333333;\">Cảm ơn bạn đã lựa chọn đăng ký tài khoản tại hệ thống chuỗi cửa hàng mỹ phẩm <strong>SkinViet_MyPham Shop</strong>. Mã OTP xác thực của bạn là:</p>"
                    + "<div style=\"text-align: center; margin: 30px 0;\">"
                    + "<span style=\"display: inline-block; font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #ffffff; background: linear-gradient(135deg, #d84374, #e85d88); padding: 12px 30px; border-radius: 8px; box-shadow: 0 4px 15px rgba(216, 67, 116, 0.3);\">" + otpCode + "</span>"
                    + "</div>"
                    + "<p style=\"font-size: 13px; color: #888888; text-align: center;\">Mã xác thực có hiệu lực trong vòng <strong>5 phút</strong>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                    + "<hr style=\"border: none; border-top: 1px solid #f1e4e8; margin-top: 25px;\">"
                    + "<p style=\"font-size: 12px; color: #aaaaaa; text-align: center; margin: 0;\">© 2026 SkinViet_MyPham Shop. Hotline: 1900 1234</p>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);

            System.out.println(">>> [THÀNH CÔNG] GOOGLE ĐÃ GỬI EMAIL OTP TỚI HỘP THƯ: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println(">>> [CẢNH BÁO GỬI EMAIL QUA GOOGLE]: " + e.getMessage());
            System.out.println(">>> [CHẾ ĐỘ DỰ PHÒNG]: Bạn có thể lấy mã OTP [ " + otpCode + " ] ở trên từ Console Eclipse để nhập xác thực!");
            return false;
        }
    }
}
