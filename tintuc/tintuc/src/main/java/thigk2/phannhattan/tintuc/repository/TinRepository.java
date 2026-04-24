package thigk2.phannhattan.tintuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thigk2.phannhattan.tintuc.model.Tin;
import java.util.List;

public interface TinRepository extends JpaRepository<Tin, Long> {
    List<Tin> findByTheLoaiTinId(Long theLoaiId);
}