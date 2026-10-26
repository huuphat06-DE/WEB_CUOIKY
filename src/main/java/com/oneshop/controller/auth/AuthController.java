package com.oneshop.controller.auth;

import com.oneshop.config.jwt.JwtUtils;
import com.oneshop.dto.RegisterDto;
import com.oneshop.entity.Branch;
import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.service.BranchService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * THÀNH VIÊN 1: Controller Xác thực, Đăng nhập (JWT Cookie) và Đăng ký tài khoản
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BranchService branchService;

    @Value("${oneshop.jwt.cookie-name:oneshop_jwt_token}")
    private String jwtCookieName;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "required", required = false) String required,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        if (error != null) model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác!");
        if (logout != null) model.addAttribute("successMessage", "Bạn đã đăng xuất thành công!");
        if (required != null) model.addAttribute("infoMessage", "Vui lòng đăng nhập để tiếp tục!");
        if (registered != null) model.addAttribute("successMessage", "Chúc mừng bạn đã đăng ký tài khoản thành công! Vui lòng đăng nhập để tiếp tục.");
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpServletResponse response,
                               Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Tìm thông tin người dùng để lấy quyền
            User user = userService.findByUsername(username).orElse(null);
            String roleName = user != null ? user.getRole().name() : "ROLE_CUSTOMER";

            // Sinh JWT Token
            String jwtToken = jwtUtils.generateToken(username, roleName);

            // Gán JWT Token vào HTTP-Only Cookie an toàn
            Cookie cookie = new Cookie(jwtCookieName, jwtToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 ngày
            response.addCookie(cookie);

            // Điều hướng theo phân quyền
            if (user != null && (user.getRole() == Role.ROLE_ADMIN || user.getRole() == Role.ROLE_MANAGER)) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";

        } catch (AuthenticationException e) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            return "auth/login";
        }
    }

    @Autowired
    private com.oneshop.service.EmailService emailService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("registerForm") RegisterDto dto, 
                                  HttpSession session,
                                  Model model) {
        try {
            // 1. Kiểm tra tính hợp lệ (username/email trùng, mật khẩu không khớp)
            userService.validateRegisterDto(dto);

            // 2. Sinh mã OTP 6 số
            String otp = emailService.generateOtp();

            // 3. Lưu thông tin đăng ký và OTP vào session (hạn 5 phút)
            session.setAttribute("PENDING_REGISTER_DTO", dto);
            session.setAttribute("REGISTER_OTP", otp);
            session.setAttribute("OTP_EXPIRE", System.currentTimeMillis() + 5 * 60 * 1000);

            // 4. Gửi email OTP (đồng thời in ra Eclipse Console)
            try {
                boolean sent = emailService.sendOtpEmail(dto.getEmail(), otp);
                if (!sent) {
                    session.setAttribute("MAIL_WARNING", "Không thể gửi thư qua Google SMTP (do kết nối mạng hoặc bảo mật Gmail). Bạn có thể lấy mã OTP [ " + otp + " ] từ tab Console của Eclipse để nhập và tiếp tục test đồ án!");
                } else {
                    session.removeAttribute("MAIL_WARNING");
                }
            } catch (Throwable t) {
                System.err.println(">>> Lỗi khi gửi mail qua Google SMTP: " + t.getMessage());
                session.setAttribute("MAIL_WARNING", "Không thể gửi thư qua Google SMTP. Bạn có thể lấy mã OTP [ " + otp + " ] từ tab Console của Eclipse để nhập và tiếp tục test đồ án!");
            }

            // 5. Chuyển sang màn hình nhập OTP
            return "redirect:/auth/verify-otp";

        } catch (Throwable e) {
            model.addAttribute("errorMessage", e.getMessage() != null ? e.getMessage() : e.toString());
            return "auth/register";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam(value = "resent", required = false) String resent,
                                HttpSession session, 
                                Model model) {
        RegisterDto pendingDto = (RegisterDto) session.getAttribute("PENDING_REGISTER_DTO");
        if (pendingDto == null) {
            return "redirect:/auth/register";
        }

        model.addAttribute("email", pendingDto.getEmail());
        if (resent != null) {
            model.addAttribute("infoMessage", "Mã OTP mới đã được khởi tạo!");
        }
        String mailWarning = (String) session.getAttribute("MAIL_WARNING");
        if (mailWarning != null) {
            model.addAttribute("mailWarning", mailWarning);
        }
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(@RequestParam("otp") String otpInput,
                                   HttpSession session,
                                   Model model) {
        RegisterDto pendingDto = (RegisterDto) session.getAttribute("PENDING_REGISTER_DTO");
        String correctOtp = (String) session.getAttribute("REGISTER_OTP");
        Long expireTime = (Long) session.getAttribute("OTP_EXPIRE");

        if (pendingDto == null || correctOtp == null || expireTime == null) {
            return "redirect:/auth/register";
        }

        // Kiểm tra thời hạn OTP (5 phút)
        if (System.currentTimeMillis() > expireTime) {
            model.addAttribute("errorMessage", "Mã OTP đã hết hạn (quá 5 phút). Vui lòng bấm 'Gửi lại mã'!");
            model.addAttribute("email", pendingDto.getEmail());
            return "auth/verify-otp";
        }

        // Kiểm tra mã OTP người dùng nhập vào
        if (!correctOtp.trim().equals(otpInput != null ? otpInput.trim() : "")) {
            model.addAttribute("errorMessage", "Mã OTP không chính xác. Vui lòng kiểm tra lại Gmail hoặc Console!");
            model.addAttribute("email", pendingDto.getEmail());
            return "auth/verify-otp";
        }

        // OTP đúng -> Tiến hành lưu User vào Database
        try {
            userService.registerCustomer(pendingDto);

            // Xóa session OTP sau khi đăng ký thành công
            session.removeAttribute("PENDING_REGISTER_DTO");
            session.removeAttribute("REGISTER_OTP");
            session.removeAttribute("OTP_EXPIRE");

            return "redirect:/auth/login?registered=true";

        } catch (Throwable e) {
            model.addAttribute("errorMessage", "Đăng ký thất bại: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
            model.addAttribute("email", pendingDto.getEmail());
            return "auth/verify-otp";
        }
    }

    @GetMapping("/resend-otp")
    public String resendOtp(HttpSession session) {
        RegisterDto pendingDto = (RegisterDto) session.getAttribute("PENDING_REGISTER_DTO");
        if (pendingDto != null) {
            String newOtp = emailService.generateOtp();
            session.setAttribute("REGISTER_OTP", newOtp);
            session.setAttribute("OTP_EXPIRE", System.currentTimeMillis() + 5 * 60 * 1000);
            try {
                boolean sent = emailService.sendOtpEmail(pendingDto.getEmail(), newOtp);
                if (!sent) {
                    session.setAttribute("MAIL_WARNING", "Không thể gửi thư qua Google SMTP. Bạn có thể lấy mã OTP [ " + newOtp + " ] trên tab Console của Eclipse để nhập và test!");
                } else {
                    session.removeAttribute("MAIL_WARNING");
                }
            } catch (Throwable t) {
                System.err.println(">>> Lỗi khi gửi lại mail: " + t.getMessage());
                session.setAttribute("MAIL_WARNING", "Không thể gửi thư qua Google SMTP. Bạn có thể lấy mã OTP [ " + newOtp + " ] trên tab Console của Eclipse để nhập và test!");
            }
            return "redirect:/auth/verify-otp?resent=true";
        }
        return "redirect:/auth/register";
    }

    @PostMapping("/select-branch")
    @ResponseBody
    public String selectBranch(@RequestParam("branchId") Long branchId, HttpSession session) {
        Branch branch = branchService.getBranchById(branchId).orElse(null);
        if (branch != null) {
            session.setAttribute("selectedBranch", branch);
            return "OK";
        }
        return "NOT_FOUND";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
