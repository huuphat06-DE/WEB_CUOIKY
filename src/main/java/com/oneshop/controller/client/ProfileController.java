package com.oneshop.controller.client;

import com.oneshop.entity.Order;
import com.oneshop.entity.User;
import com.oneshop.service.OrderService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * THÀNH VIÊN 2: Controller Trang cá nhân & Lịch sử đơn hàng của khách hàng
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String profilePage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login?required=true";
        }

        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Order> orders = orderService.getOrdersByUser(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);

        return "client/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User formUser,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user != null) {
            user.setFullName(formUser.getFullName());
            user.setPhone(formUser.getPhone());
            user.setAddress(formUser.getAddress());
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        }

        return "redirect:/profile";
    }
}
