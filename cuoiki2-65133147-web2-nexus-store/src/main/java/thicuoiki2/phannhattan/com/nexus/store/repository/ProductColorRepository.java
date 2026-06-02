package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thicuoiki2.phannhattan.com.nexus.store.entity.ProductColor;
import java.util.List;

public interface ProductColorRepository extends JpaRepository<ProductColor, Integer> {
    List<ProductColor> findByProductId(Integer productId);
}