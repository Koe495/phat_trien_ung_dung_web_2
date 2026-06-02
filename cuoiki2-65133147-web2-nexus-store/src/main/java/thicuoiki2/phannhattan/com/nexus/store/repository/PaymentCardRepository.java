package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thicuoiki2.phannhattan.com.nexus.store.entity.PaymentCard;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Integer> {
    List<PaymentCard> findByUser(User user);
}