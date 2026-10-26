package com.oneshop.repository;

import com.oneshop.entity.Branch;
import com.oneshop.entity.BranchStock;
import com.oneshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1: Repository quản lý tồn kho từng chi nhánh
 */
@Repository
public interface BranchStockRepository extends JpaRepository<BranchStock, Long> {
    Optional<BranchStock> findByBranchAndProduct(Branch branch, Product product);
    Optional<BranchStock> findByBranchIdAndProductId(Long branchId, Long productId);
    List<BranchStock> findByProduct(Product product);
    List<BranchStock> findByBranch(Branch branch);
    List<BranchStock> findByBranchId(Long branchId);
}
