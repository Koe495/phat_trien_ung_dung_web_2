# Nexus Store — Cuối kỳ Web2

Một web store mẫu (dự án cuối kỳ) cho khóa Phát triển ứng dụng Web 2. Project này bao gồm phần giao diện (HTML/CSS/JavaScript) và phần backend (Java) để quản lý sản phẩm, người dùng và đơn hàng.

## Mô tả ngắn
Nexus Store là ứng dụng web thương mại điện tử đơn giản phục vụ mục đích học tập: hiển thị sản phẩm, tìm kiếm, thêm vào giỏ hàng và mô phỏng quy trình đặt hàng. Dự án minh họa kiến trúc client-server cơ bản, CRUD cho sản phẩm, và tích hợp giao diện responsive.

## Tính năng chính
- Danh mục và danh sách sản phẩm
- Tìm kiếm và lọc sản phẩm
- Thêm/sửa/xóa sản phẩm (trong trang quản trị)
- Giỏ hàng và mô phỏng đặt hàng
- API REST (backend Java) cung cấp dữ liệu cho frontend
- Giao diện responsive (desktop & mobile)

## Công nghệ sử dụng
- Frontend: HTML, CSS, JavaScript
- Backend: Java Spring Boot
- Database: MySQL
- Công cụ build: Maven

## Cấu trúc thư mục (ví dụ)
- /src/main/java — mã nguồn Java (backend)
- /src/main/resources — cấu hình, template, mã frontend

## Yêu cầu trước khi chạy
- Java JDK 11+
- Maven
- MySQL
- Spring Boot CLI

## Cài đặt & chạy (hướng dẫn mẫu)
1. Clone repo:
   git clone https://github.com/Koe495/phat_trien_ung_dung_web_2.git
2. Vào thư mục project:
   phat_trien_ung_dung_web_2/cuoiki2-65133147-web2-nexus-store
3. Cấu hình
   - Thiết lập thông tin database mẫu trong file sql.
   - SERVER PORT mặc định 8080
4. Chạy backend (Maven):
   mvn clean package
    Dùng Spring Boot wrapper:
   ./mvnw spring-boot:run

## Cấu hình môi trường mẫu
Ví dụ file `.env` :
DB_URL=jdbc:mysql://localhost:3306/nexusdb
DB_USER=root
DB_PASS={yourpassword}
SERVER_PORT=8080

## Màn hình & Demo
Thêm ảnh chụp màn hình vào thư mục `docs/screenshots` và cập nhật phần này với link hoặc ảnh trực quan.


## Tác giả & Liên hệ
- Tác giả: Phan Nhật Tấn (Koe495)
- Email: tonphan495@gmail.com
