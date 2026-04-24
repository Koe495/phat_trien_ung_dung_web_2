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
    
    @Column(name = "ten", nullable = false)
    private String ten;

    @OneToMany(mappedBy = "theLoaiTin")
    @JsonIgnore
    private List<Tin> danhSachTin;

    // ----- GETTER & SETTER -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public List<Tin> getDanhSachTin() {
        return danhSachTin;
    }

    public void setDanhSachTin(List<Tin> danhSachTin) {
        this.danhSachTin = danhSachTin;
    }
}