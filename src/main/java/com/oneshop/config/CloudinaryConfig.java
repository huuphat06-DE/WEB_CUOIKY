package com.oneshop.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * THÀNH VIÊN 1: Cấu hình kết nối Cloudinary SDK
 */
@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name:oneshop-cosmetics}")
    private String cloudName;

    @Value("${cloudinary.api-key:123456789012345}")
    private String apiKey;

    @Value("${cloudinary.api-secret:abcdefghijklmnopqrstuvwxyz12345}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }
}
