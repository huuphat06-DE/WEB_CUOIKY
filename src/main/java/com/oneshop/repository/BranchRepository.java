package com.oneshop.repository;

import com.oneshop.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * THÀNH VIÊN 1: Repository quản lý chi nhánh chuỗi cửa hàng
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByActiveTrue();
    List<Branch> findByProvinceCityIgnoreCase(String provinceCity);
}
