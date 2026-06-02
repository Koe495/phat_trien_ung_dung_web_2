package thicuoiki2.phannhattan.com.nexus.store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;
import thicuoiki2.phannhattan.com.nexus.store.service.PaymentService;
import thicuoiki2.phannhattan.com.nexus.store.service.UserService;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    /* ------------------------------------------------------------------ */
    /*  Trang chủ                                                           */
    /* ------------------------------------------------------------------ */
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "index";
    }

    /* ------------------------------------------------------------------ */
    /*  Sản phẩm                                                            */
    /* ------------------------------------------------------------------ */
    @GetMapping("/products")
    public String products(HttpSession session, Model model) {
        List<Product> activeProducts = productRepository.findByStatus("active");
        model.addAttribute("products", activeProducts);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "products";
    }

    /* ------------------------------------------------------------------ */
    /*  Hỗ trợ                                                              */
    /* ------------------------------------------------------------------ */
    @GetMapping("/support")
    public String support(HttpSession session, Model model) {
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "support";
    }

    /* ------------------------------------------------------------------ */
    /*  Trang tài khoản người dùng                                          */
    /* ------------------------------------------------------------------ */
    @GetMapping("/user")
    public String user(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("savedCards", paymentService.getCardsByUser(loggedInUser));
        model.addAttribute("savedBanks", paymentService.getBanksByUser(loggedInUser));
        return "user";
    }

    /* ------------------------------------------------------------------ */
    /*  Cập nhật hồ sơ cá nhân                                             */
    /* ------------------------------------------------------------------ */
    @PostMapping("/user/update-profile")
    public String updateProfile(@ModelAttribute User updatedUser,
                                HttpSession session, Model model) {
        try {
            User saved = userService.updateProfile(updatedUser);
            session.setAttribute("loggedInUser", saved);
            return "redirect:/user?success=profile";
        } catch (Exception e) {
            model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
            model.addAttribute("errorMsg", e.getMessage());
            return "user";
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Đổi mật khẩu                                                        */
    /* ------------------------------------------------------------------ */
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
            return "redirect:/user?success=password";
        } catch (Exception e) {
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("errorMsg", e.getMessage());
            return "user";
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Lưu thẻ thanh toán                                                  */
    /* ------------------------------------------------------------------ */
    @PostMapping("/user/save-card")
    public String saveCard(@RequestParam String cardHolder,
                           @RequestParam String cardNumber,
                           @RequestParam String cardExpiry,
                           @RequestParam(required = false, defaultValue = "") String cardNetwork,
                           HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        try {
            paymentService.saveCard(loggedInUser, cardHolder, cardNumber, cardExpiry, cardNetwork);
            return "redirect:/user?success=card&tab=payment";
        } catch (Exception e) {
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("errorMsg", e.getMessage());
            return "user";
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Lưu tài khoản ngân hàng                                             */
    /* ------------------------------------------------------------------ */
    @PostMapping("/user/save-bank")
    public String saveBank(@RequestParam String bankName,
                           @RequestParam String bankAccount,
                           @RequestParam String bankOwner,
                           @RequestParam(required = false, defaultValue = "") String bankBranch,
                           HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        try {
            paymentService.saveBank(loggedInUser, bankName, bankAccount, bankOwner, bankBranch);
            return "redirect:/user?success=bank&tab=payment";
        } catch (Exception e) {
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("errorMsg", e.getMessage());
            return "user";
        }
    }
}