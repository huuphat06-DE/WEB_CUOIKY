package com.oneshop.controller.admin;

import com.oneshop.entity.Product;
import com.oneshop.service.CategoryService;
import com.oneshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * THÀNH VIÊN 1: Controller Quản lý sản phẩm mỹ phẩm và tích hợp Cloudinary
 */
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", categoryService.getAllBrands());
        return "admin/products/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", categoryService.getAllBrands());
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        productService.saveProduct(product, imageFile);
        redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm và upload ảnh Cloudinary thành công!");
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm!");
        return "redirect:/admin/products";
    }
}
