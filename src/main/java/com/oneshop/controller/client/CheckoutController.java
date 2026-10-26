package com.oneshop.controller.client;

import com.oneshop.dto.CartDto;
import com.oneshop.dto.OrderRequestDto;
import com.oneshop.entity.Branch;
import com.oneshop.entity.Order;
import com.oneshop.entity.User;
import com.oneshop.service.BranchService;
import com.oneshop.service.OrderService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * THÀNH VIÊN 2: Controller Thanh toán & Đặt hàng theo mô hình chuỗi
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String checkoutPage(HttpSession session, Authentication authentication, Model model) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        if (cart == null || cart.getItemList().isEmpty()) {
            return "redirect:/cart";
        }

        OrderRequestDto form = new OrderRequestDto();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                form.setCustomerName(user.getFullName());
                form.setCustomerEmail(user.getEmail());
                form.setCustomerPhone(user.getPhone());
                form.setShippingAddress(user.getAddress());
            }
        }

        List<Branch> branches = branchService.getAllActiveBranches();
        model.addAttribute("cart", cart);
        model.addAttribute("orderForm", form);
        model.addAttribute("branches", branches);

        return "client/checkout";
    }

    @PostMapping
    public String placeOrder(@ModelAttribute("orderForm") OrderRequestDto form,
                             HttpSession session,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        if (cart == null || cart.getItemList().isEmpty()) {
            return "redirect:/cart";
        }

        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userService.findByUsername(authentication.getName()).orElse(null);
        }

        try {
            Order order = orderService.placeOrder(cart, form, user);
            // Xóa sạch giỏ hàng sau khi đặt thành công
            session.removeAttribute("cart");
            return "redirect:/checkout/success/" + order.getOrderCode();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hoàn tất đặt hàng: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/success/{orderCode}")
    public String orderSuccess(@PathVariable("orderCode") String orderCode, Model model) {
        Order order = orderService.getOrderByCode(orderCode)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng: " + orderCode));
        model.addAttribute("order", order);
        return "client/order-success";
    }
}
