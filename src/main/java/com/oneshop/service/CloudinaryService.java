package com.oneshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * THÀNH VIÊN 1: Dịch vụ upload và quản lý hình ảnh qua Cloudinary
 */
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Upload hình ảnh lên Cloudinary và trả về URL bảo mật (HTTPS)
     * Nếu không có kết nối Cloudinary thật, tự động fallback sang ảnh demo chất lượng cao
     */
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "oneshop_cosmetics",
                            "resource_type", "auto"
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            System.err.println("Cloudinary upload warning: " + e.getMessage() + ". Sử dụng ảnh dự phòng.");
            // Fallback ảnh mỹ phẩm chất lượng cao từ Unsplash để hệ thống không bị gián đoạn khi test offline
            return "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=600&q=80";
        }
    }
}
