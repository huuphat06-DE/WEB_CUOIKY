package com.oneshop.repository;

import com.oneshop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * THÀNH VIÊN 2: Repository Đánh giá và bình luận mỹ phẩm
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    long countByProductId(Long productId);
}
