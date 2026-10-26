package com.oneshop.repository;

import com.oneshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1 & 2: Repository Quản lý Đơn hàng theo chuỗi chi nhánh
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByBranchIdOrderByCreatedAtDesc(Long branchId);

    List<Order> findAllByOrderByCreatedAtDesc();

    // Tổng doanh thu toàn chuỗi (Chỉ tính các đơn COMPLETED)
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'COMPLETED'")
    BigDecimal calculateTotalRevenue();

    // Doanh thu theo từng chi nhánh cụ thể
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.branch.id = :branchId AND o.status = 'COMPLETED'")
    BigDecimal calculateRevenueByBranch(@Param("branchId") Long branchId);

    // Đếm số lượng đơn theo trạng thái
    long countByStatus(Order.OrderStatus status);

    // Đếm số lượng đơn theo chi nhánh và trạng thái
    long countByBranchIdAndStatus(Long branchId, Order.OrderStatus status);
}
