package thiGKtest.ntu65133147.phannhattan_testgk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import thiGKtest.ntu65133147.phannhattan_testgk.models.Page;
import thiGKtest.ntu65133147.phannhattan_testgk.models.Post;

@Service
public class DataService {

    public static List<Page> pages = new ArrayList<>();
    public static List<Post> posts = new ArrayList<>();

    static {

        pages.add(new Page(1,"Home","home","Home page",0));
        pages.add(new Page(2,"About","about","About us",0));
        pages.add(new Page(3,"Contact","contact","Contact page",0));

        posts.add(new Post(1,"Spring Boot Tutorial","Content 1",1));
        posts.add(new Post(2,"Java Basic","Content 2",1));
        posts.add(new Post(3,"Web MVC","Content 3",2));
    }
}