package thigk2.phannhattan.tintuc.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import thigk2.phannhattan.tintuc.model.Tin;
import thigk2.phannhattan.tintuc.repository.TheLoaiTinRepository;
import thigk2.phannhattan.tintuc.repository.TinRepository;

import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private TinRepository tinRepo;

    @Autowired
    private TheLoaiTinRepository theLoaiRepo;

    @GetMapping("/tin")
    public String hienThiTatCaTin(Model model) {
        model.addAttribute("danhSachTin", tinRepo.findAll());
        model.addAttribute("theLoaiList", theLoaiRepo.findAll());
        return "danhsachtin";
    }

    @GetMapping("/tin/theloai/{id}")
    public String hienThiTinTheoTheLoai(@PathVariable("id") Long id, Model model) {
        model.addAttribute("danhSachTin", tinRepo.findByTheLoaiTinId(id));
        model.addAttribute("theLoaiList", theLoaiRepo.findAll());
        return "danhsachtin";
    }

    @GetMapping("/tin/chitiet/{id}")
    public String hienThiChiTietTin(@PathVariable("id") Long id, Model model) {
        Optional<Tin> tin = tinRepo.findById(id);
        if (tin.isPresent()) {
            model.addAttribute("tin", tin.get());
            return "chitiettin";
        }
        return "redirect:/tin";
    }
}