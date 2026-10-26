package com.oneshop.repository;

import com.oneshop.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * THÀNH VIÊN 1: Repository Thương hiệu mỹ phẩm
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBySlug(String slug);
    Optional<Brand> findByName(String name);
}
