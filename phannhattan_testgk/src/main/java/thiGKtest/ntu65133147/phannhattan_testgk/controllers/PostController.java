package thiGKtest.ntu65133147.phannhattan_testgk.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import thiGKtest.ntu65133147.phannhattan_testgk.DataService;
import thiGKtest.ntu65133147.phannhattan_testgk.models.Post;

@Controller
@RequestMapping("/post")
public class PostController {

    @GetMapping("/all")
    public String list(Model model){

        model.addAttribute("posts", DataService.posts);
        return "post-list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model){

        for(Post p : DataService.posts){
            if(p.getId()==id){
                model.addAttribute("post",p);
            }
        }

        return "post-view";
    }

    @GetMapping("/new")
    public String addForm(Model model){

        model.addAttribute("post", new Post());
        return "post-add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Post post){

        post.setId(DataService.posts.size()+1);
        DataService.posts.add(post);

        return "redirect:/post/all";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){

        DataService.posts.removeIf(p -> p.getId()==id);

        return "redirect:/post/all";
    }
}