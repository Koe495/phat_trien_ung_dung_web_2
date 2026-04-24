package thigk2.phannhattan.tintuc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tin")
public class Tin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tieuDe;
    
    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "the_loai_id")
    private TheLoaiTin theLoaiTin;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id")
    private NguoiDung nguoiDung;

}