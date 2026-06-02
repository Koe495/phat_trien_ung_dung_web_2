package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thicuoiki2.phannhattan.com.nexus.store.entity.CartItem;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProductIdAndColorId(User user, Integer productId, Integer colorId);
    void deleteByUser(User user);
}