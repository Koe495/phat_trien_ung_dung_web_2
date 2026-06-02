package thicuoiki2.phannhattan.com.nexus.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thicuoiki2.phannhattan.com.nexus.store.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
