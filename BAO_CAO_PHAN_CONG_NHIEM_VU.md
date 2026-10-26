# BÁO CÁO PHÂN CÔNG NHIỆM VỤ ĐỒ ÁN CUỐI KỲ
**MÔN HỌC: LẬP TRÌNH WEB (JAVA WEB / SPRING BOOT)**  
**ĐỀ TÀI: XÂY DỰNG WEBSITE BÁN MỸ PHẨM ONESHOP THEO MÔ HÌNH CHUỖI CỬA HÀNG**  

---

## 1. THÔNG TIN CHUNG
* **Tên đề tài**: Website thương mại điện tử mỹ phẩm OneShop theo mô hình chuỗi cửa hàng O2O.
* **Quy mô nhóm**: 2 sinh viên.
* **Thời gian thực hiện**: 4 tuần.
* **Kiến trúc công nghệ**:
  * **Backend**: Spring Boot 2.7.x, Spring Data JPA, Hibernate, Spring Security.
  * **Xác thực**: JSON Web Token (JWT) lưu trữ an toàn trong HTTP-Only Cookie.
  * **Frontend**: Thymeleaf Server-Side Rendering kết hợp Decorator Pattern Sitemesh 3, Bootstrap 5, Font Awesome 6.
  * **Cơ sở dữ liệu**: H2 Database (hỗ trợ chạy ngay) / MySQL Server.
  * **Quản lý đa phương tiện**: Cloudinary Java SDK (Lưu trữ ảnh sản phẩm trên đám mây).
  * **Biểu đồ thống kê**: Chart.js.

---

## 2. BẢNG PHÂN CHIA NHIỆM VỤ CHI TIẾT (TỶ LỆ ĐÓNG GÓP 50% - 50%)

### 👤 THÀNH VIÊN 1: BACKEND CORE, BẢO MẬT & QUẢN TRỊ CHUỖI CỬA HÀNG (ADMIN & SYSTEM)
* **Khối lượng công việc**: 50%
* **Trách nhiệm đảm nhiệm**:
  1. **Khởi tạo & Cấu trúc Dự án**: Thiết lập `pom.xml`, cấu hình kết nối Database, cấu hình Sitemesh 3 Decorator Filter.
  2. **Bảo mật & Phân quyền (Spring Security + JWT)**:
     * Viết lớp `JwtUtils.java` sinh và giải mã token.
     * Viết lớp `JwtAuthenticationFilter.java` trích xuất token từ Cookie / Header.
     * Cấu hình phân quyền đa cấp RBAC (`ROLE_ADMIN`, `ROLE_MANAGER`, `ROLE_CUSTOMER`) trong `SecurityConfig.java`.
     * Xử lý đăng nhập, cấp phát Cookie JWT an toàn và đăng xuất trong `AuthController.java`.
  3. **Quản trị Chuỗi Chi Nhánh & Kho Hàng (Multi-branch Core)**:
     * Quản lý thông tin Chi nhánh: Thêm, sửa, xóa, hiển thị chi nhánh (`AdminBranchController`).
     * Quản lý Tồn kho chuỗi (`AdminStockController` & `BranchStock`): Xem và cập nhật số lượng tồn kho của từng sản phẩm tại từng cửa hàng cụ thể.
  4. **Tích hợp Cloudinary & Quản trị Mỹ phẩm**:
     * Viết `CloudinaryService.java` tự động upload ảnh sản phẩm lên CDN đám mây.
     * Quản lý sản phẩm mỹ phẩm, thương hiệu, danh mục (`AdminProductController`).
  5. **Báo cáo Thống kê Doanh thu (Chart.js)**:
     * Viết `DashboardService.java` tính toán tổng doanh thu toàn chuỗi và doanh thu từng chi nhánh.
     * Tích hợp biểu đồ cột Chart.js trực quan hóa doanh số giữa các chi nhánh.
  6. **Giao diện Quản trị**:
     * Xây dựng layout Sitemesh Admin (`templates/decorators/admin.html`).
     * Thiết kế toàn bộ giao diện phân hệ Admin (`templates/admin/**`).

---

### 👤 THÀNH VIÊN 2: STOREFRONT & TRẢI NGHIỆM MUA SẮM KHÁCH HÀNG (CLIENT & COMMERCE)
* **Khối lượng công việc**: 50%
* **Trách nhiệm đảm nhiệm**:
  1. **Thiết kế Giao diện Mỹ phẩm Sang trọng (Luxury Cosmetics UI)**:
     * Xây dựng CSS mỹ phẩm chuyên nghiệp (`client.css`) với tone màu hồng pastel, gold và kiểu chữ thanh lịch.
     * Xây dựng layout Sitemesh Client (`templates/decorators/client.html`) với Topbar chi nhánh, Navbar, Search bar và Footer.
  2. **Trang Chủ & Khám Phá Sản Phẩm**:
     * Xây dựng `HomeController.java` và view `home.html` (Hero banner, Cam kết chính hãng 100%, Danh mục nổi bật, Sản phẩm bán chạy trong tuần, Đối tác thương hiệu).
  3. **Bộ Lọc Mỹ Phẩm Chuyên Sâu (Advanced Filter)**:
     * Xây dựng tính năng lọc thông minh theo: **Loại da** (Da dầu mụn, da khô, da nhạy cảm), Thương hiệu, Mức giá, Danh mục trong `ClientProductController.java` và `shop.html`.
     * Phân trang sản phẩm và sắp xếp theo giá.
  4. **Trang Chi Tiết Sản Phẩm & Widget Tra Cứu Tồn Kho**:
     * Hiển thị chi tiết mỹ phẩm (Dung tích, công dụng, cách dùng, giá gốc & giá sale).
     * **Widget "Kiểm tra còn hàng tại các chi nhánh"**: Bảng hiển thị tình trạng còn/hết hàng thời gian thực tại từng chi nhánh gần khách hàng.
     * Chức năng gửi đánh giá sao (1-5 sao) và bình luận trải nghiệm sản phẩm.
  5. **Giỏ Hàng & Quy Trình Thanh Toán Đa Kênh (O2O Checkout)**:
     * Giỏ hàng Ajax (`CartController.java` & `cart.html`) thêm nhanh không cần tải lại trang.
     * Trang Đặt hàng (`CheckoutController.java` & `checkout.html`): Cho phép khách chọn **Nhận tại cửa hàng (Pick-up)** hoặc **Giao hàng tận nơi (Delivery)**.
     * Tích hợp phương thức thanh toán COD hoặc VNPAY Sandbox.
  6. **Hồ Sơ Khách Hàng & Quản Lý Đơn Mua**:
     * Xây dựng `ProfileController.java` và `profile.html`: Cập nhật thông tin nhận hàng, xem lịch sử đơn mua và theo dõi trạng thái đơn (Pending, Confirmed, Shipping, Completed).

---

## 3. KỊCH BẢN DEMO BẢO VỆ ĐỒ ÁN TRƯỚC GIẢNG VIÊN

* **Bước 1: Trải nghiệm Khách hàng (Thành viên 2 thuyết trình)**
  1. Vào trang chủ `http://localhost:8080`, đổi chi nhánh phục vụ trên Topbar.
  2. Vào trang Cửa hàng, áp dụng bộ lọc chọn loại da: **"Da dầu mụn"** -> Các sản phẩm La Roche-Posay, CeraVe xuất hiện.
  3. Bấm vào chi tiết sản phẩm, kéo xuống mục **"Kiểm tra tồn kho tại các chi nhánh"** để giảng viên thấy chi nhánh Q.1 còn 15 cái, Thủ Đức còn 10 cái.
  4. Thêm sản phẩm vào giỏ hàng, bấm Tiến hành đặt hàng.
  5. Chọn hình thức: **"Nhận tại cửa hàng"** (chọn OneShop Thủ Đức, phí ship 0đ) hoặc **"Giao tận nơi"** (phí ship 30.000đ).
  6. Đặt hàng thành công -> Chuyển sang trang xem lịch sử đơn hàng.

* **Bước 2: Trải nghiệm Quản trị Chuỗi & Tồn kho (Thành viên 1 thuyết trình)**
  1. Đăng xuất tài khoản khách hàng, đăng nhập tài khoản Admin: `admin` / `admin123`.
  2. Hệ thống chuyển ngay vào **Dashboard**: Trình chiếu số liệu tổng doanh thu và **Biểu đồ cột Chart.js** so sánh doanh số giữa các chi nhánh.
  3. Vào menu **"Tồn kho chuỗi"**: Chọn chi nhánh Thủ Đức, cập nhật số lượng tồn kho cho sản phẩm vừa mua.
  4. Vào menu **"Sản phẩm mỹ phẩm"**: Thêm một sản phẩm mới, chọn ảnh tải lên -> Server upload ảnh lên **Cloudinary** và lưu URL về hệ thống.
  5. Vào menu **"Quản lý đơn hàng"**: Xem đơn hàng mới nhất của khách vừa đặt, bấm cập nhật trạng thái từ `PENDING` -> `CONFIRMED` -> `SHIPPING` -> `COMPLETED`.
  6. Giảng viên kiểm tra kỹ thuật: Giải thích cơ chế **JWT HttpOnly Cookie** trong `JwtAuthenticationFilter.java` và **Decorator Sitemesh 3** trong `SitemeshConfig.java`.
