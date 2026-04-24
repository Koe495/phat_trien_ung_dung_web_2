package thigk2.phannhattan.tintuc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "the_loai_tin")
public class TheLoaiTin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tenTheLoai;

    @OneToMany(mappedBy = "theLoaiTin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tin> danhSachTin;

}