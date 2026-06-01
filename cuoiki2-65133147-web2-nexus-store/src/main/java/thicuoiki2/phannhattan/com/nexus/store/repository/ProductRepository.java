package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Tự định nghĩa thêm hàm tìm kiếm sản phẩm theo trạng thái
    List<Product> findByStatus(String status);
}