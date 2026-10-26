package com.oneshop.service;

import com.oneshop.dto.RegisterDto;
import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1: Dịch vụ Quản lý Người dùng & Đăng ký tài khoản
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void validateRegisterDto(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại trong hệ thống!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email này đã được sử dụng!");
        }
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp!");
        }
    }

    public User registerCustomer(RegisterDto dto) {
        validateRegisterDto(dto);

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress("Chưa cập nhật");
        user.setRole(Role.ROLE_CUSTOMER);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
