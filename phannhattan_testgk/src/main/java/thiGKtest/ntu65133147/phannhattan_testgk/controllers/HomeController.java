package thiGKtest.ntu65133147.phannhattan_testgk.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard(Model model){

        model.addAttribute("username","Tấn Phan");
        model.addAttribute("content","Welcome to Dashboard");

        return "dashboard";
    }
}
