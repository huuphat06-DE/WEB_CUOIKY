package com.oneshop.service;

import com.oneshop.entity.Brand;
import com.oneshop.entity.Category;
import com.oneshop.repository.BrandRepository;
import com.oneshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1: Dịch vụ Quản lý Danh mục và Thương hiệu mỹ phẩm
 */
@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
