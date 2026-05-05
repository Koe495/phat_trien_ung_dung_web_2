package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import services.TinTucService;
import thigk2.phannhattan.tintuc.entities.TinTuc;
import thigk2.phannhattan.tintuc.repository.LoaiTinTucRepository;

@Controller
@RequestMapping("/admin/tintuc")
public class AdminController {

    @Autowired
    private TinTucService service;

    @Autowired
    private LoaiTinTucRepository loaiRepo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "admin/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("tinTuc", new TinTuc());
        model.addAttribute("loaiList", loaiRepo.findAll());
        return "admin/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute TinTuc tinTuc) {
        service.save(tinTuc);
        return "redirect:/admin/tintuc";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("tinTuc", service.findById(id));
        model.addAttribute("loaiList", loaiRepo.findAll());
        return "admin/edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/tintuc";
    }
}