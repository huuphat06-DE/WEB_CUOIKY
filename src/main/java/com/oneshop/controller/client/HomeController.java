package com.oneshop.controller.client;

import com.oneshop.entity.Brand;
import com.oneshop.entity.Category;
import com.oneshop.entity.Product;
import com.oneshop.service.CategoryService;
import com.oneshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * THÀNH VIÊN 2: Controller Trang chủ bán mỹ phẩm OneShop
 */
@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model) {
        List<Product> featuredProducts = productService.getFeaturedProducts();
        List<Product> newestProducts = productService.getNewestProducts();
        List<Category> categories = categoryService.getAllCategories();
        List<Brand> brands = categoryService.getAllBrands();

        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("newestProducts", newestProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);

        return "client/home";
    }
}
