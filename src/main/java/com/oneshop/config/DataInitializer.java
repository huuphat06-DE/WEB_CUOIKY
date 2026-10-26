package com.oneshop.config;

import com.oneshop.entity.*;
import com.oneshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * THÀNH VIÊN 1 & 2: Khởi tạo dữ liệu mẫu chuỗi cửa hàng mỹ phẩm OneShop
 * Tự động tạo tài khoản Admin, Quản lý chi nhánh, Chi nhánh, Danh mục, Thương hiệu và Tồn kho
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchStockRepository branchStockRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Đã có dữ liệu, không tạo lại
        }

        System.out.println(">>> ĐANG KHỞI TẠO DỮ LIỆU MẪU MỸ PHẨM CHO SKINVIET_MYPHAM SHOP...");

        // 1. TẠO CÁC CHI NHÁNH CHUỖI (BRANCHES)
        Branch b1 = new Branch(null, "SkinViet Quận 1", "72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP.HCM", "028.3822.1111", "TP.HCM", "08:30 - 22:30", true, null);
        Branch b2 = new Branch(null, "SkinViet Thủ Đức", "01 Võ Văn Ngân, Linh Chiểu, TP. Thủ Đức, TP.HCM", "028.3722.2222", "TP.HCM", "08:30 - 22:00", true, null);
        Branch b3 = new Branch(null, "SkinViet Cầu Giấy", "241 Xuân Thủy, Dịch Vọng Hậu, Cầu Giấy, Hà Nội", "024.3754.3333", "Hà Nội", "08:30 - 22:00", true, null);

        branchRepository.save(b1);
        branchRepository.save(b2);
        branchRepository.save(b3);

        // 2. TẠO CÁC TÀI KHOẢN NGƯỜI DÙNG (USERS)
        // Admin tối cao
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Chủ Tịch SkinViet");
        admin.setEmail("admin@skinviet.vn");
        admin.setPhone("0909000001");
        admin.setRole(Role.ROLE_ADMIN);
        userRepository.save(admin);

        // Quản lý chi nhánh Quận 1
        User managerQ1 = new User();
        managerQ1.setUsername("manager_q1");
        managerQ1.setPassword(passwordEncoder.encode("manager123"));
        managerQ1.setFullName("Quản Lý CN Quận 1");
        managerQ1.setEmail("manager_q1@skinviet.vn");
        managerQ1.setPhone("0909000002");
        managerQ1.setRole(Role.ROLE_MANAGER);
        managerQ1.setManagedBranch(b1);
        userRepository.save(managerQ1);

        // Khách hàng mẫu
        User customer = new User();
        customer.setUsername("customer1");
        customer.setPassword(passwordEncoder.encode("123456"));
        customer.setFullName("Nguyễn Thảo My");
        customer.setEmail("thaomy@gmail.com");
        customer.setPhone("0912345678");
        customer.setAddress("45 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP.HCM");
        customer.setRole(Role.ROLE_CUSTOMER);
        userRepository.save(customer);

        // 3. TẠO DANH MỤC MỸ PHẨM (CATEGORIES)
        Category c1 = new Category(null, "Chăm sóc da mặt", "cham-soc-da-mat", "Serum, kem dưỡng, toner, kem chống nắng", "https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=300&q=80");
        Category c2 = new Category(null, "Trang điểm", "trang-diem", "Son môi, phấn nền, mascara, kem lót", "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=300&q=80");
        Category c3 = new Category(null, "Làm sạch da", "lam-sach-da", "Nước tẩy trang, sữa rửa mặt, tẩy tế bào chết", "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&w=300&q=80");
        Category c4 = new Category(null, "Nước hoa cao cấp", "nuoc-hoa", "Nước hoa EDP, EDT từ các thương hiệu hàng đầu", "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?auto=format&fit=crop&w=300&q=80");

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);
        categoryRepository.save(c4);

        // 4. TẠO THƯƠNG HIỆU MỸ PHẨM (BRANDS)
        Brand br1 = new Brand(null, "La Roche-Posay", "Pháp", "https://upload.wikimedia.org/wikipedia/commons/4/4e/La_Roche-Posay_logo.svg", "Dược mỹ phẩm hàng đầu nước Pháp cho da nhạy cảm");
        Brand br2 = new Brand(null, "Innisfree", "Hàn Quốc", "", "Mỹ phẩm thiên nhiên từ đảo Jeju xinh đẹp");
        Brand br3 = new Brand(null, "CeraVe", "Mỹ", "", "Phục hồi hàng rào bảo vệ da với 3 Ceramide thiết yếu");
        Brand br4 = new Brand(null, "MAC Cosmetics", "Mỹ", "", "Đẳng cấp son môi và trang điểm chuyên nghiệp");
        Brand br5 = new Brand(null, "Kiehl's", "Mỹ", "", "Dược phẩm lâu đời từ New York năm 1851");

        brandRepository.save(br1);
        brandRepository.save(br2);
        brandRepository.save(br3);
        brandRepository.save(br4);
        brandRepository.save(br5);

        // 5. TẠO CÁC SẢN PHẨM MỸ PHẨM THỰC TẾ (PRODUCTS)
        Product p1 = new Product();
        p1.setSku("LSP-SERUM-EFF");
        p1.setName("Serum La Roche-Posay Effaclar Giảm Mụn & Thâm Mụn");
        p1.setSlug("serum-la-roche-posay-effaclar");
        p1.setOriginalPrice(new BigDecimal("895000"));
        p1.setSalePrice(new BigDecimal("760000"));
        p1.setSkinType("Da dầu mụn");
        p1.setUsageCategory("Trị mụn & Mờ thâm");
        p1.setVolume("30ml");
        p1.setOrigin("Pháp");
        p1.setDescription("Serum chứa phức hợp 3 acid chuyên sâu: LHA, Salicylic Acid và Glycolic Acid giúp thông thoáng lỗ chân lông, làm mịn bề mặt da và giảm rõ rệt các vết thâm sau mụn chỉ sau 28 ngày sử dụng.");
        p1.setMainImage("https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=600&q=80");
        p1.setFeatured(true);
        p1.setCategory(c1);
        p1.setBrand(br1);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setSku("CRV-CLEANSER-473");
        p2.setName("Sữa Rửa Mặt CeraVe Foaming Facial Cleanser");
        p2.setSlug("sua-rua-mat-cerave-foaming");
        p2.setOriginalPrice(new BigDecimal("450000"));
        p2.setSalePrice(new BigDecimal("380000"));
        p2.setSkinType("Da dầu mụn");
        p2.setUsageCategory("Làm sạch sâu");
        p2.setVolume("473ml");
        p2.setOrigin("Mỹ");
        p2.setDescription("Sữa rửa mặt tạo bọt dịu nhẹ được bác sĩ da liễu khuyên dùng, chứa 3 Ceramide thiết yếu, Niacinamide và Hyaluronic Acid giúp làm sạch bã nhờn mà không phá vỡ màng ẩm tự nhiên.");
        p2.setMainImage("https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=600&q=80");
        p2.setFeatured(true);
        p2.setCategory(c3);
        p2.setBrand(br3);
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setSku("MAC-LIP-CHILI");
        p3.setName("Son Thỏi MAC Matte Lipstick Màu Chili Đỏ Gạch");
        p3.setSlug("son-mac-matte-chili");
        p3.setOriginalPrice(new BigDecimal("690000"));
        p3.setSalePrice(new BigDecimal("590000"));
        p3.setSkinType("Mọi loại da");
        p3.setUsageCategory("Trang điểm môi");
        p3.setVolume("3g");
        p3.setOrigin("Mỹ");
        p3.setDescription("Màu son Chili đỏ gạch huyền thoại của MAC tôn da tuyệt đối, chất son lì mịn như nhung bám màu lâu trôi suốt cả ngày dài.");
        p3.setMainImage("https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=600&q=80");
        p3.setFeatured(true);
        p3.setCategory(c2);
        p3.setBrand(br4);
        productRepository.save(p3);

        Product p4 = new Product();
        p4.setSku("INN-SERUM-GREENTEA");
        p4.setName("Tinh Chất Dưỡng Ẩm Innisfree Green Tea Seed Serum");
        p4.setSlug("tinh-chat-innisfree-green-tea");
        p4.setOriginalPrice(new BigDecimal("600000"));
        p4.setSalePrice(new BigDecimal("490000"));
        p4.setSkinType("Da khô");
        p4.setUsageCategory("Cấp ẩm chuyên sâu");
        p4.setVolume("80ml");
        p4.setOrigin("Hàn Quốc");
        p4.setDescription("Chiết xuất từ lá trà xanh tươi tại đảo Jeju giàu Amino Acid và khoáng chất, cấp ẩm tức thì và tạo màng khóa ẩm vượt trội.");
        p4.setMainImage("https://images.unsplash.com/photo-1608248597359-009772a5a544?auto=format&fit=crop&w=600&q=80");
        p4.setFeatured(true);
        p4.setCategory(c1);
        p4.setBrand(br2);
        productRepository.save(p4);

        Product p5 = new Product();
        p5.setSku("LSP-ANTHELIOS-50");
        p5.setName("Kem Chống Nắng La Roche-Posay Anthelios UVMune 400");
        p5.setSlug("kem-chong-nang-la-roche-posay");
        p5.setOriginalPrice(new BigDecimal("535000"));
        p5.setSalePrice(new BigDecimal("445000"));
        p5.setSkinType("Da nhạy cảm");
        p5.setUsageCategory("Bảo vệ da chống nắng");
        p5.setVolume("50ml");
        p5.setOrigin("Pháp");
        p5.setDescription("Màng lọc độc quyền Mexoryl 400 bảo vệ da toàn diện khỏi tia UVA dài gây nám và lão hóa sớm, kiềm dầu kháng nước cực đỉnh.");
        p5.setMainImage("https://images.unsplash.com/photo-1567928815116-419b67484df4?auto=format&fit=crop&w=600&q=80");
        p5.setFeatured(false);
        p5.setCategory(c1);
        p5.setBrand(br1);
        productRepository.save(p5);

        // 6. PHÂN BỔ TỒN KHO CHO TỪNG CHI NHÁNH CHUỖI (BRANCH STOCKS)
        // Chi nhánh Q1
        branchStockRepository.save(new BranchStock(b1, p1, 15));
        branchStockRepository.save(new BranchStock(b1, p2, 20));
        branchStockRepository.save(new BranchStock(b1, p3, 8));
        branchStockRepository.save(new BranchStock(b1, p4, 12));
        branchStockRepository.save(new BranchStock(b1, p5, 25));

        // Chi nhánh Thủ Đức
        branchStockRepository.save(new BranchStock(b2, p1, 10));
        branchStockRepository.save(new BranchStock(b2, p2, 14));
        branchStockRepository.save(new BranchStock(b2, p3, 0)); // Tạm hết son MAC ở Thủ Đức
        branchStockRepository.save(new BranchStock(b2, p4, 6));
        branchStockRepository.save(new BranchStock(b2, p5, 18));

        // Chi nhánh Cầu Giấy (Hà Nội)
        branchStockRepository.save(new BranchStock(b3, p1, 12));
        branchStockRepository.save(new BranchStock(b3, p2, 18));
        branchStockRepository.save(new BranchStock(b3, p3, 5));
        branchStockRepository.save(new BranchStock(b3, p4, 9));
        branchStockRepository.save(new BranchStock(b3, p5, 15));

        // 7. TẠO ĐƠN HÀNG MẪU ĐỂ CÓ SẴN DOANH THU & BIỂU ĐỒ (ORDERS)
        Order sampleOrder1 = new Order();
        sampleOrder1.setOrderCode("ONE-20261026-9812");
        sampleOrder1.setUser(customer);
        sampleOrder1.setBranch(b1);
        sampleOrder1.setCustomerName("Nguyễn Thảo My");
        sampleOrder1.setCustomerPhone("0912345678");
        sampleOrder1.setCustomerEmail("thaomy@gmail.com");
        sampleOrder1.setShippingAddress("45 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP.HCM");
        sampleOrder1.setOrderType(Order.OrderType.DELIVERY);
        sampleOrder1.setStatus(Order.OrderStatus.COMPLETED);
        sampleOrder1.setPaymentMethod(Order.PaymentMethod.COD);
        sampleOrder1.setPaymentStatus(Order.PaymentStatus.PAID);
        sampleOrder1.setTotalAmount(new BigDecimal("1170000"));
        sampleOrder1.setShippingFee(new BigDecimal("30000"));
        sampleOrder1.setCreatedAt(LocalDateTime.now().minusDays(1));
        orderRepository.save(sampleOrder1);

        OrderItem oi1 = new OrderItem(sampleOrder1, p1, p1.getSalePrice(), 1);
        OrderItem oi2 = new OrderItem(sampleOrder1, p2, p2.getSalePrice(), 1);
        orderItemRepository.save(oi1);
        orderItemRepository.save(oi2);

        Order sampleOrder2 = new Order();
        sampleOrder2.setOrderCode("ONE-20261026-9813");
        sampleOrder2.setUser(customer);
        sampleOrder2.setBranch(b2);
        sampleOrder2.setCustomerName("Trần Hoàng Nam");
        sampleOrder2.setCustomerPhone("0987654321");
        sampleOrder2.setOrderType(Order.OrderType.PICKUP);
        sampleOrder2.setShippingAddress("Nhận tại chi nhánh OneShop Thủ Đức");
        sampleOrder2.setStatus(Order.OrderStatus.CONFIRMED);
        sampleOrder2.setPaymentMethod(Order.PaymentMethod.VNPAY);
        sampleOrder2.setPaymentStatus(Order.PaymentStatus.PAID);
        sampleOrder2.setTotalAmount(new BigDecimal("760000"));
        sampleOrder2.setShippingFee(BigDecimal.ZERO);
        sampleOrder2.setCreatedAt(LocalDateTime.now().minusHours(3));
        orderRepository.save(sampleOrder2);

        OrderItem oi3 = new OrderItem(sampleOrder2, p1, p1.getSalePrice(), 1);
        orderItemRepository.save(oi3);

        System.out.println(">>> KHỞI TẠO DỮ LIỆU ONESHOP HOÀN TẤT!");
        System.out.println(">>> ĐÃ TẠO 3 CHI NHÁNH, 5 SẢN PHẨM MỸ PHẨM, TỒN KHO & ĐƠN HÀNG MẪU.");
    }
}
