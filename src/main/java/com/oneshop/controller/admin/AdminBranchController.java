package com.oneshop.controller.admin;

import com.oneshop.entity.Branch;
import com.oneshop.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * THÀNH VIÊN 1: Controller Quản lý các chi nhánh trong chuỗi cửa hàng
 */
@Controller
@RequestMapping("/admin/branches")
public class AdminBranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public String listBranches(Model model) {
        model.addAttribute("branches", branchService.getAllBranches());
        return "admin/branches/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("branch", new Branch());
        return "admin/branches/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Branch branch = branchService.getBranchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi nhánh: " + id));
        model.addAttribute("branch", branch);
        return "admin/branches/form";
    }

    @PostMapping("/save")
    public String saveBranch(@ModelAttribute("branch") Branch branch, RedirectAttributes redirectAttributes) {
        branchService.saveBranch(branch);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu thông tin chi nhánh thành công!");
        return "redirect:/admin/branches";
    }

    @GetMapping("/delete/{id}")
    public String deleteBranch(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        branchService.deleteBranch(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa chi nhánh!");
        return "redirect:/admin/branches";
    }
}
