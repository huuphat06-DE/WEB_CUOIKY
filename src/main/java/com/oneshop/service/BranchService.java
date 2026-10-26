package com.oneshop.service;

import com.oneshop.entity.Branch;
import com.oneshop.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1: Dịch vụ Quản lý Chi nhánh chuỗi OneShop
 */
@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public List<Branch> getAllActiveBranches() {
        return branchRepository.findByActiveTrue();
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }
}
