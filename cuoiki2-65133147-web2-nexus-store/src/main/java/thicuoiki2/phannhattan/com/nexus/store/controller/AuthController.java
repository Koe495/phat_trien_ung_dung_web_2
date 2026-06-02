package thicuoiki2.phannhattan.com.nexus.store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Hiển thị trang Đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Gọi tới file register.html
    }

    // 2. Xử lý logic Đăng ký tài khoản gửi lên từ Form
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerNewUser(user);
            // Đăng ký thành công, điều hướng về đăng nhập kèm thông báo
            model.addAttribute("successMsg", "Đăng ký tài khoản thành công! Hãy đăng nhập ngay.");
            return "login";
        } catch (Exception e) {
            // Đăng ký thất bại (Trùng email), hiển thị lại trang kèm thông báo lỗi
            model.addAttribute("errorMsg", e.getMessage());
            return "register";
        }
    }

    // 3. Hiển thị trang Đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Gọi tới file login.html
    }

    // 4. Xử lý logic Đăng nhập (Dùng Session để duy trì trạng thái đăng nhập)
    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        try {
            User authenticatedUser = userService.authenticateUser(email, password);
            
            // Lưu thông tin người dùng đã xác thực vào Session của Servlet
            session.setAttribute("loggedInUser", authenticatedUser);
            
            if ("admin".equalsIgnoreCase(authenticatedUser.getRole())) {
                return "redirect:/admin";
            }

            // Quay về trang chủ sau khi đăng nhập thành công
            return "redirect:/";
        } catch (Exception e) {
            // Đăng nhập thất bại, trả về giao diện đăng nhập kèm cảnh báo lỗi
            model.addAttribute("errorMsg", e.getMessage());
            return "login";
        }
    }

    // 5. Đăng xuất tài khoản
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Hủy toàn bộ Session hiện tại
        return "redirect:/";
    }
}