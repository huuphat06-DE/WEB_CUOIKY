package com.oneshop.config;

import com.oneshop.config.jwt.JwtAuthenticationFilter;
import com.oneshop.config.jwt.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.Cookie;

/**
 * THÀNH VIÊN 1: Cấu hình Spring Security 5 + JWT Filter
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers().frameOptions().disable() // Cho phép hiển thị H2 console
            .and()
            .authenticationProvider(authenticationProvider())
            .authorizeRequests()
                // Phân hệ Quản trị (Admin & Quản lý chi nhánh)
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                // Phân hệ Khách hàng cần đăng nhập
                .antMatchers("/profile/**", "/checkout/**", "/api/order/**").authenticated()
                // Tất cả các trang công khai (Trang chủ, Chi tiết SP, Tìm kiếm, Đăng nhập, Assets)
                .antMatchers("/", "/shop/**", "/product/**", "/cart/**", "/auth/**", "/h2-console/**",
                             "/css/**", "/js/**", "/images/**", "/fonts/**", "/webjars/**").permitAll()
                .anyRequest().permitAll()
            .and()
            // Chuyển hướng khi chưa đăng nhập mà truy cập trang bảo mật
            .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/auth/login?required=true");
                })
                .accessDeniedPage("/auth/access-denied")
            .and()
            // Xử lý Đăng xuất: Xóa Cookie JWT
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    Cookie cookie = new Cookie("oneshop_jwt_token", null);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    response.sendRedirect("/auth/login?logout=true");
                })
                .permitAll();

        // Gắn JWT Filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
