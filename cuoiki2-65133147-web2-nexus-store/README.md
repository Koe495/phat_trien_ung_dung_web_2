# Nexus Store

[Demo (n/a)](https://example.com/demo) • Status: Work in progress

---

## 1. Mô tả ứng dụng
Nexus Store là một ứng dụng thương mại điện tử (online store) mẫu được xây dựng bằng Spring Boot và Thymeleaf. Ứng dụng mô phỏng các chức năng cơ bản của cửa hàng trực tuyến: duyệt sản phẩm, giỏ hàng, thanh toán đơn hàng và quản trị sản phẩm/đơn hàng dành cho Admin. Mục tiêu của dự án là làm nền tảng học tập/tiệm cận thực tế cho các bài tập web backend Java.

---

## 2. Điểm nổi bật
- Hệ thống phân quyền cơ bản (Admin / Customer).
- Quản lý sản phẩm, danh mục, đơn hàng.
- Giỏ hàng và chức năng checkout.
- API tìm kiếm sản phẩm trả về JSON.
- Template engine: Thymeleaf (server-side rendering).

---

## 3. Công nghệ sử dụng
- Java 17
- Spring Boot 4.0.6
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL (chính)
- H2 Console (runtime, tùy config)
- Lombok
- Maven (wrapper: mvnw)

---

## 4. Các chức năng chính
### Người dùng (Customer)
- Đăng ký / đăng nhập / đăng xuất
- Xem trang chủ, danh sách sản phẩm, chi tiết sản phẩm
- Tìm kiếm sản phẩm: `GET /api/search?q=...`
- Quản lý giỏ hàng: thêm, cập nhật số lượng, xóa sản phẩm
- Thanh toán (tạo đơn hàng)
- Quản lý hồ sơ (profile) và thông tin thanh toán

### Quản trị (Admin)
- Giao diện quản trị: `/admin`
- Quản lý người dùng: xem, tạo, sửa, xóa
- Quản lý sản phẩm & danh mục: xem, tạo, sửa, xóa, lọc
- Quản lý đơn hàng: xem, cập nhật trạng thái, xóa

---

## 5. Ảnh minh họa giao diện

### Trang khách hàng (Customer)

**Trang chủ**
![Trang chủ](src/main/resources/static/img/MainPage.png)

**Sản phẩm nổi bật**
![Sản phẩm nổi bật](src/main/resources/static/img/SanPhamNoiBat.png)

**Danh sách sản phẩm, tìm kiếm & lọc theo danh mục**
![Danh sách sản phẩm / Tìm kiếm / SideNav](src/main/resources/static/img/MainPageSanPham_Search_SideNav.png)

**Trang danh sách sản phẩm**
![Trang sản phẩm](src/main/resources/static/img/TrangSanPham.png)

**Chi tiết sản phẩm**
![Chi tiết sản phẩm](src/main/resources/static/img/SanPhamDetail.png)

**Giỏ hàng**
![Giỏ hàng](src/main/resources/static/img/GioHang.png)

**Thanh toán / Đặt hàng**
![Thanh toán](src/main/resources/static/img/ThanhToan.png)

**Hồ sơ người dùng**
![Hồ sơ](src/main/resources/static/img/HoSo.png)

**Trang hỗ trợ**
![Trang hỗ trợ](src/main/resources/static/img/TrangHoTro.png)

### Trang quản trị (Admin)

**Quản lý sản phẩm**
![Admin - Sản phẩm](src/main/resources/static/img/AdminProducts.png)

**Quản lý danh mục**
![Admin - Danh mục](src/main/resources/static/img/AdminDanhMuc.png)

**Quản lý đơn hàng**
![Admin - Đơn hàng](src/main/resources/static/img/AdminDonHang.png)

**Quản lý người dùng**
![Admin - Người dùng](src/main/resources/static/img/AdminUser.png)

---

## 6. Kiến trúc tổng quan

Ứng dụng được thiết kế theo mô hình phân tầng (MVC + Service + Repository).  
Tóm tắt luồng: Browser ↔ Controller ↔ Service ↔ Repository ↔ Database.

Ví dụ sơ đồ (text):

```
Browser / Client
    ↓ HTTP
Presentation (Thymeleaf templates)
    ↓
Spring Security (filter)
    ↓
Controllers (HomeController, ProductController, CartController, AuthController, AdminController)
    ↓
Services (ProductService, CartService, OrderService, UserService)
    ↓
Repositories (Spring Data JPA)
    ↓
MySQL Database
```

---

## 7. Cấu trúc thư mục chính
```
src/
├── main/
│   ├── java/
│   │   └── thicuoiki2/phannhattan/com/nexus/store/
│   │       ├── NexusStoreApplication.java
│   │       ├── config/         # SecurityConfig, WebConfig...
│   │       ├── controller/     # Controllers
│   │       ├── entity/         # JPA entities
│   │       ├── repository/     # Spring Data repositories
│   │       └── service/        # Business logic
│   └── resources/
│       ├── templates/          # Thymeleaf views
│       ├── static/             # css, js, images
│       └── application.properties
```

---

## 8. Yêu cầu (Prerequisites)
- Java 17+ JDK
- Maven (hoặc dùng wrapper `./mvnw`)
- MySQL server đang chạy (hoặc thay bằng H2 cho dev)
- Git (để clone repo)

---

## 9. Quick Start — Chạy nhanh dự án (Local)
1. Clone repository
```bash
git clone https://github.com/Koe495/phat_trien_ung_dung_web_2.git
cd cuoiki2-65133147-web2-nexus-store
```

2. Tạo database MySQL (ví dụ tên `nexus_db`)
```sql
CREATE DATABASE nexus_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Cập nhật cấu hình kết nối DB
- Mở `src/main/resources/application.properties`
- Cập nhật:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nexus_db?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```
Gợi ý: để tiện, tạo file `application.properties` từ `application.properties.example` (nếu có).

4. Chạy ứng dụng (Windows)
```bash
.\mvnw.cmd -DskipTests spring-boot:run
```
Hoặc (Unix/macOS)
```bash
./mvnw -DskipTests spring-boot:run
```
Hoặc build jar và chạy
```

5. Mở trình duyệt: http://localhost:8080

---

## 10. Seed dữ liệu & Tài khoản Admin
- Nếu repo có file seed (ví dụ `nexusdb.sql`), import vào database
---

## 11. Endpoints chính (tổng quan)
- Public / User:
  - GET / — Trang chủS
  - GET /products — Danh sách sản phẩm
  - GET /product/{slug} — Chi tiết sản phẩm
  - GET /cart — Giỏ hàng
  - POST /cart/add — Thêm vào giỏ
  - POST /cart/update — Cập nhật số lượng
  - POST /cart/remove — Xóa sản phẩm khỏi giỏ
  - GET /checkout — Trang thanh toán
  - POST /checkout/place-order — Đặt hàng
  - GET /register, POST /register
  - GET /login, POST /login
- Admin (ví dụ đường dẫn):
  - GET /admin
  - POST /admin/products/save
  - POST /admin/products/delete
  - POST /admin/users/save
  - POST /admin/orders/update-status
- API:
  - GET /api/search?q=... — Tìm kiếm trả về JSON

---

## 12. Cấu hình bảo mật và lưu ý production
- Hiện `SecurityConfig` có thể đang được cấu hình để dễ phát triển (ví dụ `permitAll()` hoặc CSRF disabled). KHÔNG sử dụng cấu hình này trong môi trường production.
- Đối với production:
  - Sử dụng HTTPS.
  - Quản lý bí mật (DB password, JWT secret...) qua biến môi trường hoặc vault.
  - `spring.jpa.hibernate.ddl-auto` nên để `validate` hoặc tắt cho production (không dùng `update`).
  - Bật CSRF, kiểm tra session fixation, rate limiting, validation đầu vào.

---

## 13. Test & Dev tools
- Chạy unit/integration tests:
```bash
mvn test
```
- IDE: Eclipse / IntelliJ IDEA / VS Code (Extension Pack for Java, Spring Boot Extension Pack)
- Mẹo debug: bật `spring.jpa.show-sql=true` để xem SQL, sử dụng H2 console cho debugging nhanh.

---

## 14. Troubleshooting (lỗi thường gặp)
- Lỗi kết nối DB:
  - Kiểm tra URL, username, password, port.
  - Kiểm tra MySQL đã chạy và DB được tạo.
- Lỗi port 8080 bị chiếm:
  - Thay `server.port` trong `application.properties`.
- Lỗi migration / schema:
  - Kiểm tra `spring.jpa.hibernate.ddl-auto` hoặc seed SQL.

---

## 15. Hướng nâng cấp & Tính năng tương lai
- Hoàn thiện phân quyền với Spring Security (ROLE_ADMIN / ROLE_USER) và bảo mật CSRF.
- Thêm API REST riêng biệt (Spring Web) cho SPA/ứng dụng mobile.
- Thêm upload hình ảnh sản phẩm (storage service).
- Thêm chức năng email thông báo (Spring Mail).
- Thêm CI (GitHub Actions) và Docker image build/publish.

---

## 16. Đóng góp & Liên hệ
- Đóng góp: tạo issue / pull request trên GitHub.
- Vui lòng mô tả rõ thay đổi, kèm hướng dẫn chạy nếu cần.
- Tác giả: Koe495 (https://github.com/Koe495)

---