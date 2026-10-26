package com.oneshop.controller.client;

import com.oneshop.entity.*;
import com.oneshop.repository.ReviewRepository;
import com.oneshop.service.BranchService;
import com.oneshop.service.CategoryService;
import com.oneshop.service.ProductService;
import com.oneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * THÀNH VIÊN 2: Controller Mua sắm, Bộ lọc mỹ phẩm thông minh & Chi tiết sản phẩm
 */
@Controller
public class ClientProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    /**
     * Trang Cửa hàng với bộ lọc chuyên sâu cho mỹ phẩm:
     * Loại da (Da dầu, Da khô, Da mụn, Da nhạy cảm), Thương hiệu, Giá, Danh mục
     */
    @GetMapping("/shop")
    public String shopPage(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                           @RequestParam(value = "brandId", required = false) Long brandId,
                           @RequestParam(value = "skinType", required = false) String skinType,
                           @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                           @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "sortBy", defaultValue = "newest") String sortBy,
                           Model model) {

        Page<Product> productPage = productService.filterProducts(
                keyword, categoryId, brandId, skinType, minPrice, maxPrice, page, 12, sortBy
        );

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        // Dữ liệu cho các thanh lọc bên sidebar
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", categoryService.getAllBrands());
        model.addAttribute("selectedKeyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("selectedSkinType", skinType);
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);
        model.addAttribute("selectedSortBy", sortBy);

        return "client/shop";
    }

    /**
     * Trang Chi tiết sản phẩm mỹ phẩm kèm Widget "Kiểm tra tồn kho tại các chi nhánh"
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + id));

        List<BranchStock> stocks = productService.getStocksByProduct(product);
        List<Branch> allBranches = branchService.getAllActiveBranches();
        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);

        model.addAttribute("product", product);
        model.addAttribute("stocks", stocks);
        model.addAttribute("allBranches", allBranches);
        model.addAttribute("reviews", reviews);

        return "client/detail";
    }

    /**
     * Gửi đánh giá sao và bình luận cho sản phẩm
     */
    @PostMapping("/product/{id}/review")
    public String submitReview(@PathVariable("id") Long id,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login?required=true";
        }

        Product product = productService.getProductById(id).orElse(null);
        User user = userService.findByUsername(authentication.getName()).orElse(null);

        if (product != null && user != null) {
            Review review = new Review();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(Math.max(1, Math.min(5, rating)));
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(review);
            redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã gửi đánh giá cho sản phẩm!");
        }

        return "redirect:/product/" + id;
    }
}
