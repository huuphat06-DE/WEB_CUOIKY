package com.oneshop.controller.admin;

import com.oneshop.entity.Branch;
import com.oneshop.entity.Product;
import com.oneshop.service.BranchService;
import com.oneshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * THÀNH VIÊN 1: Controller Quản lý tồn kho đa chi nhánh (Branch Inventory)
 */
@Controller
@RequestMapping("/admin/stocks")
public class AdminStockController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BranchService branchService;

    @GetMapping
    public String viewStocks(@RequestParam(value = "branchId", required = false) Long branchId, Model model) {
        List<Branch> branches = branchService.getAllActiveBranches();
        if (branchId == null && !branches.isEmpty()) {
            branchId = branches.get(0).getId();
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("branches", branches);
        model.addAttribute("selectedBranchId", branchId);
        model.addAttribute("products", products);
        return "admin/stocks/list";
    }

    @PostMapping("/update")
    public String updateStock(@RequestParam("branchId") Long branchId,
                              @RequestParam("productId") Long productId,
                              @RequestParam("quantity") int quantity,
                              RedirectAttributes redirectAttributes) {
        productService.updateBranchStock(branchId, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật tồn kho thành công!");
        return "redirect:/admin/stocks?branchId=" + branchId;
    }
}
