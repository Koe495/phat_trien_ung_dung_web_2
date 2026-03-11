package com.example.thuchanh1B3.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public class HomeController {

    // View -> Controller
    @PostMapping("/submit")
    public String submit(@RequestParam String name, Model model) {

        String result = "Hello " + name + "! Data received from View.";

        model.addAttribute("message", result);

        return "home";
    }

}
