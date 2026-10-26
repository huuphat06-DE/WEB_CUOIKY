package com.oneshop.repository;

import com.oneshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1 & 2: Repository Sản phẩm mỹ phẩm với bộ lọc chuyên sâu
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    Optional<Product> findBySlug(String slug);

    List<Product> findByActiveTrue();

    List<Product> findTop8ByFeaturedTrueAndActiveTrueOrderByIdDesc();

    List<Product> findTop8ByActiveTrueOrderByIdDesc();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByBrandIdAndActiveTrue(Long brandId);

    /**
     * BỘ LỌC MỸ PHẨM CHUYÊN BIỆT (THÀNH VIÊN 2 PHỤ TRÁCH UI/FILTER):
     * Lọc theo Từ khóa, Danh mục, Thương hiệu, Loại da (Da dầu/Da mụn/Da khô/Da nhạy cảm), Khoảng giá
     */
    @Query("SELECT p FROM Product p WHERE p.active = true " +
           "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
           "AND (:skinType IS NULL OR p.skinType LIKE CONCAT('%', :skinType, '%')) " +
           "AND (:minPrice IS NULL OR p.salePrice >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice)")
    Page<Product> filterProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("skinType") String skinType,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}
