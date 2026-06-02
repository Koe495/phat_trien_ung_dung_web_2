# Nexus Store

Nexus Store là ứng dụng thương mại điện tử đơn giản xây dựng bằng Spring Boot và Thymeleaf. Dự án mô phỏng cửa hàng trực tuyến với giỏ hàng, thanh toán, tài khoản người dùng và trang quản trị admin.

## Công nghệ chính

- Java 17
- Spring Boot 4.0.6
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- H2 Console (dependency runtime)
- Lombok
- Maven

## Tính năng chính

### Người dùng
- Đăng ký / đăng nhập / đăng xuất
- Xem trang chủ, danh sách sản phẩm, chi tiết sản phẩm
- Tìm kiếm sản phẩm qua API `/api/search?q=...`
- Quản lý giỏ hàng: thêm, cập nhật số lượng, xóa sản phẩm
- Thanh toán đơn hàng
- Quản lý tài khoản:
  - cập nhật profile
  - đổi mật khẩu
  - lưu thẻ thanh toán
  - lưu tài khoản ngân hàng
  - xóa thông tin thanh toán

### Admin
- Trang quản trị `/admin`
- Quản lý người dùng: xem, thêm, sửa, xóa
- Quản lý sản phẩm: xem, thêm, sửa, xóa
- Quản lý danh mục sản phẩm: xem, thêm, sửa, xóa
- Quản lý đơn hàng: xem, đổi trạng thái, xóa
- Bộ lọc admin:
  - lọc user theo tên / email / điện thoại / role
  - lọc sản phẩm theo tên / slug / mô tả / trạng thái / danh mục
  - lọc đơn hàng theo trạng thái và nội dung

## Cấu trúc dự án

- `src/main/java/thicuoiki2/phannhattan/com/nexus/store/`
  - `controller/` - controller xử lý request
  - `entity/` - các thực thể JPA
  - `repository/` - repository JPA
  - `service/` - logic nghiệp vụ
  - `config/` - cấu hình Spring Security

- `src/main/resources/`
  - `templates/` - Thymeleaf HTML
  - `static/css/` - CSS
  - `static/js/` - JavaScript
  - `application.properties` - cấu hình ứng dụng

## Cấu hình database

Tập tin `src/main/resources/application.properties` đang sử dụng MySQL:

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/nexus_db?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Lưu ý:
- Database mặc định là MySQL.
- Nếu cần, thay đổi URL, username, password.
- `spring.jpa.hibernate.ddl-auto=update` sẽ tự động cập nhật schema.

## Chạy dự án

### Yêu cầu
- Java 17
- Maven
- MySQL đang chạy
- Database `nexus_db` đã tồn tại (có thể dùng `nexus2.sql` để tạo dữ liệu ban đầu)

### Lệnh chạy
```bash
cd e:/eclipse-workspace/phat_trien_ung_dung_web_2/cuoiki2-65133147-web2-nexus-store
.\mvnw.cmd -DskipTests spring-boot:run
```

Hoặc:
```bash
mvn clean package
java -jar target/cuoiki2-65133147-web2-nexus-store-0.0.1-SNAPSHOT.jar
```

Truy cập:
- `http://localhost:8080/`

## Endpoint chính

### Trang người dùng
- `GET /` — trang chủ
- `GET /products` — danh sách sản phẩm
- `GET /product/{slug}` — chi tiết sản phẩm
- `GET /cart` — giỏ hàng
- `POST /cart/add` — thêm sản phẩm vào giỏ
- `POST /cart/update` — cập nhật số lượng
- `POST /cart/remove` — xóa sản phẩm
- `GET /checkout` — trang thanh toán
- `POST /checkout/place-order` — đặt hàng
- `GET /support` — trang hỗ trợ
- `GET /user` — trang tài khoản

### Quản lý tài khoản
- `POST /user/update-profile`
- `POST /user/change-password`
- `POST /user/save-card`
- `POST /user/save-bank`
- `POST /user/delete-payment`

### Xác thực
- `GET /register`
- `POST /register`
- `GET /login`
- `POST /login`
- `GET /logout`

### Admin
- `GET /admin`
- `POST /admin/users/save`
- `POST /admin/users/delete`
- `POST /admin/products/save`
- `POST /admin/products/delete`
- `POST /admin/categories/save`
- `POST /admin/categories/delete`
- `POST /admin/orders/update-status`
- `POST /admin/orders/delete`

### API tìm kiếm
- `GET /api/search?q=...` — trả về JSON kết quả tìm kiếm

## Ghi chú kỹ thuật

- `SecurityConfig` hiện cho phép toàn bộ request (`permitAll`) và tắt CSRF để dễ test form.
- Xác thực được quản lý bằng session servlet (`loggedInUser`).
- Quyền admin được xác định bằng `User.role == "admin"`.

## Gợi ý cải tiến
- Hoàn thiện cấu hình Spring Security phân quyền thực sự
- Thêm validation form chi tiết ở phía server và client
- Bổ sung xử lý lỗi và thông báo rõ ràng hơn
- Cải thiện giao diện responsive
```