# ONESHOP - WEBSITE BÁN MỸ PHẨM THEO MÔ HÌNH CHUỖI CỬA HÀNG
> **Đồ Án Cuối Kỳ Môn Lập Trình Web (HCMUTE)**  
> **Nhóm thực hiện**: 2 sinh viên  
> **Công nghệ sử dụng**: Spring Boot + Thymeleaf + Bootstrap 5 + JPA + Sitemesh 3 + JWT + Cloudinary  

---

## I. TỔNG QUAN DỰ ÁN
**OneShop** là hệ thống website bán lẻ mỹ phẩm O2O (Online-to-Offline) hoạt động theo **mô hình chuỗi cửa hàng đa chi nhánh**. Dự án giải quyết trọn vẹn bài toán quản lý và kinh doanh mỹ phẩm trong thực tế:
* **Quản lý tồn kho theo từng chi nhánh**: Mỗi cửa hàng có kho hàng độc lập, tự động cập nhật khi khách mua hoặc nhập hàng.
* **Đặt hàng linh hoạt**: Khách hàng có thể chọn **Nhận tại cửa hàng (Pick-up)** hoặc **Giao hàng tận nơi (Delivery)**.
* **Bộ lọc mỹ phẩm chuyên sâu**: Lọc theo loại da (Da dầu mụn, da khô, da nhạy cảm), công dụng, thương hiệu và mức giá.
* **Bảo mật JWT kết hợp Thymeleaf SSR**: Cấp phát JWT token lưu trong HttpOnly Cookie bảo mật tối đa.
* **Quản lý ảnh bằng Cloudinary**: Upload ảnh sản phẩm, album ảnh chi tiết lên Cloud.
* **Biểu đồ thống kê doanh thu (Chart.js)**: So sánh doanh số giữa các chi nhánh chuỗi trực quan.

---

## II. BẢNG PHÂN CHIA NHIỆM VỤ 2 THÀNH VIÊN

| Tiêu chí | 👤 THÀNH VIÊN 1: Backend Lead & Quản Trị Chuỗi | 👤 THÀNH VIÊN 2: Storefront & Trải Nghiệm Khách Hàng |
| :--- | :--- | :--- |
| **Vai trò chính** | Quản lý kiến trúc hệ thống, Cơ sở dữ liệu, Bảo mật JWT, Quản lý chuỗi chi nhánh & Admin Dashboard. | Thiết kế giao diện mỹ phẩm, Sitemesh Client, Bộ lọc loại da, Giỏ hàng, Đặt hàng & Đánh giá. |
| **Bảo mật & Cấu hình** | • `SecurityConfig.java`, `JwtUtils.java`, `JwtAuthenticationFilter.java`.<br>• Cấu hình Sitemesh Filter.<br>• Tích hợp SDK `CloudinaryService.java`. | • Cấu hình CSS mỹ phẩm cao cấp (`client.css`).<br>• Thư viện giao diện Bootstrap 5, Font Awesome, Google Fonts. |
| **Cơ sở dữ liệu & Entity** | • `Branch.java`, `BranchStock.java`, `User.java`, `Role.java`.<br>• `Category.java`, `Brand.java`, `Product.java`.<br>• Viết các Repository Spring Data JPA. | • `Order.java`, `OrderItem.java`, `Review.java`.<br>• `CartDto.java`, `CartItemDto.java`, `OrderRequestDto.java`. |
| **Nghiệp vụ chức năng** | • CRUD Chi nhánh cửa hàng (`AdminBranchController`).<br>• Quản lý tồn kho theo chi nhánh (`AdminStockController`).<br>• Thêm/sửa mỹ phẩm upload Cloudinary (`AdminProductController`).<br>• Thống kê doanh thu biểu đồ Chart.js (`AdminDashboardController`).<br>• Xử lý đăng nhập / đăng ký JWT Cookie (`AuthController`). | • Trang chủ (`HomeController` - Banner, Khuyến mãi, Best-sellers).<br>• Bộ lọc mỹ phẩm thông minh theo loại da (`ClientProductController`).<br>• Widget tra cứu tồn kho chi nhánh tại trang chi tiết SP.<br>• Giỏ hàng Ajax (`CartController`).<br>• Đặt hàng chọn chi nhánh / giao tận nhà (`CheckoutController`).<br>• Hồ sơ cá nhân & Tracking đơn mua (`ProfileController`). |
| **Giao diện HTML/Thymeleaf** | • `decorators/admin.html` (Layout Sidebar Admin).<br>• `admin/dashboard.html` (Biểu đồ doanh thu).<br>• `admin/branches/**` (Quản lý chi nhánh).<br>• `admin/stocks/**` (Quản lý kho chi nhánh).<br>• `admin/products/**` (Quản lý sản phẩm).<br>• `admin/orders/**` (Quản lý đơn hàng).<br>• `auth/login.html`, `auth/register.html`. | • `decorators/client.html` (Layout Storefront).<br>• `client/home.html` (Trang chủ mỹ phẩm).<br>• `client/shop.html` (Cửa hàng & Bộ lọc loại da).<br>• `client/detail.html` (Chi tiết & Tra cứu kho).<br>• `client/cart.html` (Giỏ hàng).<br>• `client/checkout.html` (Thanh toán).<br>• `client/order-success.html`.<br>• `client/profile.html` (Lịch sử đơn mua). |

---

## III. TÀI KHOẢN MẪU ĐỂ CHẤM ĐIỂM / DÙNG THỬ

Hệ thống đã có sẵn `DataInitializer` tự động nạp dữ liệu mẫu mỹ phẩm và tài khoản khi khởi chạy:

| Vai trò | Tên đăng nhập (Username) | Mật khẩu (Password) | Chức năng chính |
| :--- | :--- | :--- | :--- |
| **Admin Tổng Chuỗi** | `admin` | `admin123` | Xem biểu đồ doanh thu toàn chuỗi, quản lý tất cả chi nhánh, quản lý kho, duyệt mọi đơn hàng. |
| **Quản Lý Chi Nhánh Q.1** | `manager_q1` | `manager123` | Quản lý đơn hàng và tồn kho riêng của chi nhánh Quận 1. |
| **Khách Hàng Mẫu** | `customer1` | `123456` | Mua sắm, đặt hàng online, kiểm tra tồn kho cửa hàng, theo dõi đơn hàng đã mua. |

---

## IV. HƯỚNG DẪN MỞ VÀ CHẠY DỰ ÁN (1-CLICK)

### 1. Mở dự án trong IntelliJ IDEA / Eclipse / STS
1. Mở IDE (IntelliJ IDEA / Eclipse / STS / VS Code).
2. Chọn **Open Project** -> Trỏ tới thư mục `d:/WEB_CK`.
3. IDE sẽ tự động nhận diện file `pom.xml` và tải toàn bộ thư viện về.
4. Mở file `src/main/java/com/oneshop/OneShopApplication.java`.
5. Bấm chuột phải chọn **Run 'OneShopApplication'**.

### 2. Các đường dẫn truy cập
* **Trang chủ Khách hàng**: `http://localhost:8080`
* **Cửa hàng mỹ phẩm & Bộ lọc**: `http://localhost:8080/shop`
* **Trang Quản trị Admin**: `http://localhost:8080/admin/dashboard`
* **Trang Đăng nhập**: `http://localhost:8080/auth/login`
* **H2 Database Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:oneshopdb`, User: `sa`, Pass: để trống).

### 3. Chuyển đổi sang MySQL (Nếu muốn dùng MySQL thật)
Mở file `src/main/resources/application.properties`, bỏ dấu `#` ở mục MySQL và điền user/password của MySQL của bạn:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oneshop_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
```
