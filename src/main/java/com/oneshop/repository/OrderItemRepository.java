package com.oneshop.repository;

import com.oneshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * THÀNH VIÊN 1: Repository Chi tiết sản phẩm trong đơn
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}
