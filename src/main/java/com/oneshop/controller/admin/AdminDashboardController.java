package com.oneshop.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.service.DashboardService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * THÀNH VIÊN 1: Controller Dashboard Quản trị & Biểu đồ doanh thu chuỗi
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping({"", "/dashboard"})
    public String dashboard(Authentication authentication, Model model) throws JsonProcessingException {
        Long branchId = null;
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            if (user != null && user.getRole() == Role.ROLE_MANAGER && user.getManagedBranch() != null) {
                branchId = user.getManagedBranch().getId();
                model.addAttribute("managedBranch", user.getManagedBranch());
            }
        }

        Map<String, Object> stats = dashboardService.getDashboardStats(branchId);
        model.addAllAttributes(stats);

        // Chuyển đổi dữ liệu sang JSON cho Chart.js hiển thị trên trang Dashboard
        if (stats.containsKey("branchLabels")) {
            model.addAttribute("branchLabelsJson", objectMapper.writeValueAsString(stats.get("branchLabels")));
            model.addAttribute("branchRevenuesJson", objectMapper.writeValueAsString(stats.get("branchRevenues")));
        }

        return "admin/dashboard";
    }
}
