package com.oneshop.controller.admin;

import com.oneshop.entity.Order;
import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.service.OrderService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * THÀNH VIÊN 1: Controller Quản lý Đơn hàng theo chuỗi cửa hàng
 */
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listOrders(Authentication authentication, Model model) {
        List<Order> orders;
        if (authentication != null) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            if (user != null && user.getRole() == Role.ROLE_MANAGER && user.getManagedBranch() != null) {
                // Quản lý chi nhánh chỉ xem đơn của chi nhánh mình
                orders = orderService.getOrdersByBranch(user.getManagedBranch().getId());
                model.addAttribute("isManager", true);
                model.addAttribute("managedBranch", user.getManagedBranch());
            } else {
                orders = orderService.getAllOrders();
            }
        } else {
            orders = orderService.getAllOrders();
        }

        model.addAttribute("orders", orders);
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng: " + id));
        model.addAttribute("order", order);
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") Order.OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái đơn hàng thành: " + status);
        return "redirect:/admin/orders/" + id;
    }
}
