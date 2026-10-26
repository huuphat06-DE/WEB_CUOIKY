package com.oneshop.service;

import com.oneshop.entity.Branch;
import com.oneshop.entity.BranchStock;
import com.oneshop.entity.Product;
import com.oneshop.repository.BranchRepository;
import com.oneshop.repository.BranchStockRepository;
import com.oneshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * THÀNH VIÊN 1 & 2: Dịch vụ Sản phẩm mỹ phẩm & Tồn kho chuỗi
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchStockRepository branchStockRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findTop8ByFeaturedTrueAndActiveTrueOrderByIdDesc();
    }

    public List<Product> getNewestProducts() {
        return productRepository.findTop8ByActiveTrueOrderByIdDesc();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    /**
     * Bộ lọc mỹ phẩm phục vụ trang Shop của Thành viên 2
     */
    public Page<Product> filterProducts(String keyword, Long categoryId, Long brandId,
                                       String skinType, BigDecimal minPrice, BigDecimal maxPrice,
                                       int page, int size, String sortBy) {
        Sort sort = Sort.by("id").descending();
        if ("priceAsc".equals(sortBy)) {
            sort = Sort.by("salePrice").ascending();
        } else if ("priceDesc".equals(sortBy)) {
            sort = Sort.by("salePrice").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.filterProducts(keyword, categoryId, brandId, skinType, minPrice, maxPrice, pageable);
    }

    /**
     * Lưu thông tin sản phẩm và upload ảnh lên Cloudinary
     */
    public Product saveProduct(Product product, MultipartFile mainImageFile) {
        if (mainImageFile != null && !mainImageFile.isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(mainImageFile);
            if (uploadedUrl != null) {
                product.setMainImage(uploadedUrl);
            }
        }
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Cập nhật số lượng tồn kho của một sản phẩm tại một chi nhánh cụ thể
     */
    public BranchStock updateBranchStock(Long branchId, Long productId, int quantity) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi nhánh: " + branchId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + productId));

        BranchStock stock = branchStockRepository.findByBranchAndProduct(branch, product)
                .orElse(new BranchStock(branch, product, 0));

        stock.setQuantity(Math.max(0, quantity));
        return branchStockRepository.save(stock);
    }

    public List<BranchStock> getStocksByProduct(Product product) {
        return branchStockRepository.findByProduct(product);
    }

    public List<BranchStock> getStocksByBranch(Long branchId) {
        return branchStockRepository.findByBranchId(branchId);
    }
}
