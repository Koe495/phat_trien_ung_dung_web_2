package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import services.TinTucService;

@Controller
public class HomeController {

    @Autowired
    private TinTucService service;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("list", service.findAll());
        return "home";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("tinTuc", service.findById(id));
        return "detail";
    }
}