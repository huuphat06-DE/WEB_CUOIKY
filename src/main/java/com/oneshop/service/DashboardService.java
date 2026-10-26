package com.oneshop.service;

import com.oneshop.entity.Branch;
import com.oneshop.entity.Order;
import com.oneshop.repository.BranchRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * THÀNH VIÊN 1: Dịch vụ Thống kê Dashboard & Biểu đồ doanh thu chuỗi (Chart.js)
 */
@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getDashboardStats(Long branchId) {
        Map<String, Object> stats = new HashMap<>();

        if (branchId == null) {
            // Thống kê toàn chuỗi (Dành cho Quản trị viên tối cao ADMIN)
            BigDecimal totalRevenue = orderRepository.calculateTotalRevenue();
            stats.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
            stats.put("totalOrders", orderRepository.count());
            stats.put("totalProducts", productRepository.count());
            stats.put("totalBranches", branchRepository.count());
            stats.put("totalUsers", userRepository.count());
            stats.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
            stats.put("completedOrders", orderRepository.countByStatus(Order.OrderStatus.COMPLETED));

            // Biểu đồ so sánh doanh thu giữa các chi nhánh
            List<Branch> branches = branchRepository.findAll();
            List<String> branchLabels = new ArrayList<>();
            List<BigDecimal> branchRevenues = new ArrayList<>();

            for (Branch branch : branches) {
                branchLabels.add(branch.getName());
                BigDecimal rev = orderRepository.calculateRevenueByBranch(branch.getId());
                branchRevenues.add(rev != null ? rev : BigDecimal.ZERO);
            }
            stats.put("branchLabels", branchLabels);
            stats.put("branchRevenues", branchRevenues);

        } else {
            // Thống kê riêng cho 1 chi nhánh (Dành cho STORE_MANAGER)
            BigDecimal branchRevenue = orderRepository.calculateRevenueByBranch(branchId);
            stats.put("totalRevenue", branchRevenue != null ? branchRevenue : BigDecimal.ZERO);
            stats.put("totalOrders", orderRepository.findByBranchIdOrderByCreatedAtDesc(branchId).size());
            stats.put("pendingOrders", orderRepository.countByBranchIdAndStatus(branchId, Order.OrderStatus.PENDING));
            stats.put("completedOrders", orderRepository.countByBranchIdAndStatus(branchId, Order.OrderStatus.COMPLETED));
        }

        return stats;
    }
}
