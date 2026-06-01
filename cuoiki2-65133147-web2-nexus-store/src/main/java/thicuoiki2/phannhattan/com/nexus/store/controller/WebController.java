package thicuoiki2.phannhattan.com.nexus.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model) {
        // Lấy tất cả sản phẩm có trạng thái 'active' từ Database
        List<Product> activeProducts = productRepository.findByStatus("active");
        
        // Gắn danh sách này vào biến "products" để gửi sang giao diện Thymeleaf
        model.addAttribute("products", activeProducts);
        
        return "products"; 
    }

    @GetMapping("/support")
    public String support() {
        return "support";
    }
}