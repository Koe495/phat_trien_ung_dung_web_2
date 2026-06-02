package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thicuoiki2.phannhattan.com.nexus.store.entity.PaymentBank;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import java.util.List;

public interface PaymentBankRepository extends JpaRepository<PaymentBank, Integer> {
    List<PaymentBank> findByUser(User user);
}