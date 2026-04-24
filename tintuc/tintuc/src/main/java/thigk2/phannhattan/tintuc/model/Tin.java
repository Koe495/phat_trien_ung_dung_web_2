package thigk2.phannhattan.tintuc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tin")
public class Tin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "the_loai_id")
    private TheLoaiTin theLoaiTin;

    // ----- GETTER & SETTER -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public TheLoaiTin getTheLoaiTin() {
        return theLoaiTin;
    }

    public void setTheLoaiTin(TheLoaiTin theLoaiTin) {
        this.theLoaiTin = theLoaiTin;
    }
}