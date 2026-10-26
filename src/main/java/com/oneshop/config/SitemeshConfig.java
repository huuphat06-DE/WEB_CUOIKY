package com.oneshop.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * THÀNH VIÊN 1 & 2: Cấu hình Decorator Sitemesh 3
 * Bọc layout chuẩn cho phân hệ Khách hàng (Client) và Quản trị (Admin)
 */
@Configuration
public class SitemeshConfig {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                // Decorator trang Quản trị chuỗi (Admin / Store Manager)
                builder.addDecoratorPath("/admin/**", "/decorators/admin")
                       .addDecoratorPath("/admin", "/decorators/admin");

                // Decorator trang Khách hàng (Storefront)
                builder.addDecoratorPath("/*", "/decorators/client")
                       .addDecoratorPath("/shop/**", "/decorators/client")
                       .addDecoratorPath("/product/**", "/decorators/client")
                       .addDecoratorPath("/cart/**", "/decorators/client")
                       .addDecoratorPath("/checkout/**", "/decorators/client")
                       .addDecoratorPath("/profile/**", "/decorators/client");

                // Không áp dụng decorator cho trang đăng nhập, h2 console, api, tài nguyên tĩnh
                builder.addExcludedPath("/auth/**")
                       .addExcludedPath("/h2-console/**")
                       .addExcludedPath("/api/**")
                       .addExcludedPath("/css/**")
                       .addExcludedPath("/js/**")
                       .addExcludedPath("/images/**");
            }
        });
        filter.addUrlPatterns("/*");
        filter.setOrder(1);
        return filter;
    }
}
