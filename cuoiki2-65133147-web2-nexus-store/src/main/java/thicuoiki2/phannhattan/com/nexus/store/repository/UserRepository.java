package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // Tìm kiếm người dùng bằng Email phục vụ đăng nhập và kiểm tra trùng lặp
    Optional<User> findByEmail(String email);
    
    // Kiểm tra nhanh email đã tồn tại hay chưa
    boolean existsByEmail(String email);
}