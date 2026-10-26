package com.oneshop.repository;

import com.oneshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * THÀNH VIÊN 1: Repository Danh mục sản phẩm
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    Optional<Category> findByName(String name);
}
