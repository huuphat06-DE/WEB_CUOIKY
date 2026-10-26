package com.oneshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * THÀNH VIÊN 1 & 2: Thực thể Sản phẩm mỹ phẩm
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku; // Mã SKU: LSP-SERUM-01

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 200)
    private String slug;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal originalPrice; // Giá gốc

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice; // Giá khuyến mãi

    @Column(length = 100)
    private String skinType = "Mọi loại da"; // Da dầu mụn, Da khô, Da nhạy cảm, Da hỗn hợp

    @Column(length = 100)
    private String usageCategory = "Dưỡng ẩm & Phục hồi"; // Trị mụn, Chống nắng, Cấp ẩm, Làm sáng

    @Column(length = 50)
    private String volume = "50ml"; // Dung tích / Trọng lượng

    @Column(length = 100)
    private String origin = "Chính hãng";

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String mainImage; // Ảnh đại diện chính (Lưu từ Cloudinary)

    private boolean featured = false; // Sản phẩm bán chạy / nổi bật

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BranchStock> branchStocks = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Tính tổng số lượng tồn kho trên toàn bộ chuỗi
    public int getTotalStock() {
        if (branchStocks == null || branchStocks.isEmpty()) {
            return 0;
        }
        return branchStocks.stream().mapToInt(BranchStock::getQuantity).sum();
    }

    // Lấy số lượng tồn kho tại 1 chi nhánh cụ thể
    public int getStockAtBranch(Long branchId) {
        if (branchStocks == null || branchId == null) {
            return 0;
        }
        return branchStocks.stream()
                .filter(bs -> bs.getBranch() != null && branchId.equals(bs.getBranch().getId()))
                .mapToInt(BranchStock::getQuantity)
                .findFirst()
                .orElse(0);
    }

    // Standard Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public String getSkinType() { return skinType; }
    public void setSkinType(String skinType) { this.skinType = skinType; }

    public String getUsageCategory() { return usageCategory; }
    public void setUsageCategory(String usageCategory) { this.usageCategory = usageCategory; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }

    public List<BranchStock> getBranchStocks() { return branchStocks; }
    public void setBranchStocks(List<BranchStock> branchStocks) { this.branchStocks = branchStocks; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}
