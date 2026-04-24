package thigk2.phannhattan.tintuc.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import thigk2.phannhattan.tintuc.model.TheLoaiTin;
import thigk2.phannhattan.tintuc.model.Tin;
import thigk2.phannhattan.tintuc.repository.TheLoaiTinRepository;
import thigk2.phannhattan.tintuc.repository.TinRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TinTucApiController {

    @Autowired
    private TheLoaiTinRepository theLoaiTinRepo;

    @Autowired
    private TinRepository tinRepo;

    @GetMapping("/theloai")
    public List<TheLoaiTin> layDanhSachTheLoai() {
        return theLoaiTinRepo.findAll();
    }

    @GetMapping("/tin/theloai/{id}")
    public List<Tin> layTinTheoTheLoai(@PathVariable("id") Long id) {
        return tinRepo.findByTheLoaiTinId(id);
    }
}