package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thicuoiki2.phannhattan.com.nexus.store.entity.Order;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}