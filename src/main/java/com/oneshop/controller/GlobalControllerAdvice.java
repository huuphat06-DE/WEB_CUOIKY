package com.oneshop.controller;

import com.oneshop.dto.CartDto;
import com.oneshop.entity.Branch;
import com.oneshop.entity.Category;
import com.oneshop.entity.User;
import com.oneshop.service.BranchService;
import com.oneshop.service.CategoryService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * THÀNH VIÊN 1 & 2: Cung cấp dữ liệu dùng chung cho toàn bộ giao diện Thymeleaf
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private BranchService branchService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @ModelAttribute("globalBranches")
    public List<Branch> getGlobalBranches() {
        return branchService.getAllActiveBranches();
    }

    @ModelAttribute("globalCategories")
    public List<Category> getGlobalCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return userService.findByUsername(auth.getName()).orElse(null);
        }
        return null;
    }

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        return cart != null ? cart.getTotalQuantity() : 0;
    }

    @ModelAttribute("selectedBranch")
    public Branch getSelectedBranch(HttpSession session) {
        Branch branch = (Branch) session.getAttribute("selectedBranch");
        if (branch == null) {
            List<Branch> branches = branchService.getAllActiveBranches();
            if (!branches.isEmpty()) {
                branch = branches.get(0);
                session.setAttribute("selectedBranch", branch);
            }
        }
        return branch;
    }
}
