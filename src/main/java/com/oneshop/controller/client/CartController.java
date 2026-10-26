package com.oneshop.controller.client;

import com.oneshop.dto.CartDto;
import com.oneshop.dto.CartItemDto;
import com.oneshop.entity.Product;
import com.oneshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * THÀNH VIÊN 2: Controller Giỏ hàng mỹ phẩm OneShop
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    private CartDto getCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartDto();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartDto cart = getCart(session);
        model.addAttribute("cart", cart);
        return "client/cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam("productId") Long productId,
                                         @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Product product = productService.getProductById(productId).orElse(null);

        if (product == null) {
            response.put("success", false);
            response.put("message", "Sản phẩm không tồn tại!");
            return response;
        }

        CartDto cart = getCart(session);
        CartItemDto item = new CartItemDto(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getMainImage(),
                product.getSalePrice(),
                quantity
        );
        cart.addItem(item);
        session.setAttribute("cart", cart);

        response.put("success", true);
        response.put("message", "Đã thêm " + product.getName() + " vào giỏ hàng!");
        response.put("cartCount", cart.getTotalQuantity());
        response.put("totalAmount", cart.getTotalAmount());
        return response;
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session) {
        CartDto cart = getCart(session);
        cart.updateQuantity(productId, quantity);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeItem(@PathVariable("productId") Long productId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        CartDto cart = getCart(session);
        cart.removeItem(productId);
        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("infoMessage", "Đã xóa sản phẩm khỏi giỏ hàng!");
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }
}
