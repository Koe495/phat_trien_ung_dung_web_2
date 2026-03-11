package thiGKtest.ntu65133147.phannhattan_testgk.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thiGKtest.ntu65133147.phannhattan_testgk.models.Page;
import thiGKtest.ntu65133147.phannhattan_testgk.models.Post;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard(Model model){

        model.addAttribute("username","Tấn Phan");
        model.addAttribute("content","Welcome to Dashboard");

        ArrayList<Page> dsTrang = new ArrayList<Page>();
        ArrayList<Post> dsBaiViet = new ArrayList<Post>();
        
        return "dashboard";
    }
}
