package thiGKtest.ntu65133147.phannhattan_testgk.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import thiGKtest.ntu65133147.phannhattan_testgk.DataService;
import thiGKtest.ntu65133147.phannhattan_testgk.models.Page;


@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/all")
    public String list(Model model){

        model.addAttribute("pages", DataService.pages);
        return "page-list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model){

        for(Page p : DataService.pages){
            if(p.getId()==id){
                model.addAttribute("page",p);
            }
        }

        return "page-view";
    }

    @GetMapping("/new")
    public String addForm(Model model){

        model.addAttribute("page", new Page());
        return "page-add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Page page){

        page.setId(DataService.pages.size()+1);
        DataService.pages.add(page);

        return "redirect:/page/all";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){

        DataService.pages.removeIf(p -> p.getId()==id);

        return "redirect:/page/all";
    }
}