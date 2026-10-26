package com.oneshop.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * THÀNH VIÊN 1: Filter chặn mọi request để kiểm tra JWT Token (hỗ trợ cả API Bearer Token và Cookie cho Thymeleaf)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Value("${oneshop.jwt.cookie-name:oneshop_jwt_token}")
    private String jwtCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Không thể xác thực người dùng bằng JWT: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Trích xuất JWT:
     * 1. Ưu tiên đọc từ HTTP Cookie (phù hợp tuyệt đối với Thymeleaf SSR)
     * 2. Hoặc đọc từ Header Authorization: Bearer <token> (nếu gọi Ajax/REST API)
     */
    private String parseJwt(HttpServletRequest request) {
        // 1. Kiểm tra trong Cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (jwtCookieName.equals(cookie.getName())) {
                    if (StringUtils.hasText(cookie.getValue())) {
                        return cookie.getValue();
                    }
                }
            }
        }

        // 2. Kiểm tra trong Header Authorization
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
