package com.example.thuchanh1B2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	// Controller -> View
    @GetMapping("/")
    public String home(Model model) {

        String message = "Tin nhan tu controller";

        model.addAttribute("message", message);

        return "home";
    }
    
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }
    
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable int id) {
        return "User ID = " + id;
    }
    
    
}