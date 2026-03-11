package com.example.thuchanh1B4.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BMIController {

    @GetMapping("/bmi")
    public String showForm() {
        return "bmi";
    }

    @PostMapping("/bmi")
    public String calculateBMI(
            @RequestParam double height,
            @RequestParam double weight,
            Model model) {

        double bmi = weight / (height * height);

        String category;

        if (bmi < 18.5) {
            category = "Underweight";
        } else if (bmi < 25) {
            category = "Normal";
        } else if (bmi < 30) {
            category = "Overweight";
        } else {
            category = "Obese";
        }

        model.addAttribute("bmi", bmi);
        model.addAttribute("category", category);

        return "bmi";
    }
}