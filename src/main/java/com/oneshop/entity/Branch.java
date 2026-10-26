package com.oneshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * THÀNH VIÊN 1: Thực thể Chi nhánh chuỗi cửa hàng mỹ phẩm OneShop
 */
@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name; // Ví dụ: OneShop Quận 1, OneShop Thủ Đức, OneShop Cầu Giấy

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 100)
    private String provinceCity; // Tỉnh / Thành phố (TP.HCM, Hà Nội, Đà Nẵng...)

    @Column(length = 100)
    private String openHours = "08:30 - 22:00";

    private boolean active = true;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BranchStock> branchStocks = new ArrayList<>();

    // Standard Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getProvinceCity() { return provinceCity; }
    public void setProvinceCity(String provinceCity) { this.provinceCity = provinceCity; }

    public String getOpenHours() { return openHours; }
    public void setOpenHours(String openHours) { this.openHours = openHours; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<BranchStock> getBranchStocks() { return branchStocks; }
    public void setBranchStocks(List<BranchStock> branchStocks) { this.branchStocks = branchStocks; }
}
