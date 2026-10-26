package com.oneshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ĐỀ TÀI: XÂY DỰNG WEBSITE BÁN MỸ PHẨM ONESHOP THEO MÔ HÌNH CHUỖI CỬA HÀNG
 * Công nghệ: Spring Boot + Thymeleaf + Bootstrap + JPA + Sitemesh 3 + JWT + Cloudinary
 * Phân chia nhiệm vụ:
 * - Thành viên 1: Kiến trúc Backend, Bảo mật JWT, Quản trị chuỗi chi nhánh, Quản lý kho, Admin Dashboard
 * - Thành viên 2: Trải nghiệm người dùng, Giao diện Storefront, Bộ lọc mỹ phẩm, Giỏ hàng & Đặt hàng
 */
@SpringBootApplication
public class OneShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneShopApplication.class, args);
        System.out.println("==================================================================");
        System.out.println(">>> WEBSITE MỸ PHẨM ONESHOP ĐÃ KHỞI CHẠY THÀNH CÔNG! <<<");
        System.out.println(">>> TRANG CHỦ KHÁCH HÀNG:    http://localhost:8080");
        System.out.println(">>> TRANG QUẢN TRỊ ADMIN:    http://localhost:8080/admin/dashboard");
        System.out.println(">>> H2 DATABASE CONSOLE:     http://localhost:8080/h2-console");
        System.out.println("==================================================================");
    }
}
