package com.oneshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * THÀNH VIÊN 1: DTO Đăng ký tài khoản (Đã bỏ trường địa chỉ theo yêu cầu)
 */
@Getter
@Setter
public class RegisterDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String email;
    private String phone;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
