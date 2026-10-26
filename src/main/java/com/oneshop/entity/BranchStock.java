package com.oneshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * THÀNH VIÊN 1: Cốt lõi mô hình chuỗi - Tồn kho của từng sản phẩm tại từng chi nhánh cụ thể
 */
@Entity
@Table(name = "branch_stocks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"branch_id", "product_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity = 0; // Số lượng tồn kho tại chi nhánh này

    public BranchStock(Branch branch, Product product, int quantity) {
        this.branch = branch;
        this.product = product;
        this.quantity = quantity;
    }

    // Standard Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
