package thicuoiki2.phannhattan.com.nexus.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;
import thicuoiki2.phannhattan.com.nexus.store.service.UserService;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
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
    
    @GetMapping("/user")
    public String user(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        model.addAttribute("loggedInUser", loggedInUser);
        return "user";
    }

    @PostMapping("/user/update-profile")
    public String updateProfile(@ModelAttribute User updatedUser,
                                 HttpSession session, Model model) {
        try {
            User saved = userService.updateProfile(updatedUser);
            session.setAttribute("loggedInUser", saved); // cập nhật session
            return "redirect:/user?tab=profile&success=profile";
        } catch (Exception e) {
            model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
            model.addAttribute("errorMsg", e.getMessage());
            return "user";
        }
    }

    @PostMapping("/user/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                  @RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        try {
            if (!newPassword.equals(confirmPassword)) {
                throw new Exception("Mật khẩu xác nhận không khớp!");
            }
            userService.changePassword(loggedInUser.getId(), currentPassword, newPassword);
            return "redirect:/user?tab=security&success=password";
        } catch (Exception e) {
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("errorMsg", e.getMessage());
            return "user";
        }
    }
}