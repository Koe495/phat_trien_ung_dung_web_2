package thicuoiki2.phannhattan.com.nexus.store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;
import thicuoiki2.phannhattan.com.nexus.store.service.CartService;
import thicuoiki2.phannhattan.com.nexus.store.service.PaymentService;
import thicuoiki2.phannhattan.com.nexus.store.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserService        userService;
    @Autowired private PaymentService     paymentService;
    @Autowired private CartService        cartService;

    /* Helper dùng chung: tính tổng số lượng hàng trong giỏ */
    private void addCartCount(User user, Model model) {
        if (user != null) {
            int count = cartService.getCartItems(user).stream()
                    .mapToInt(item -> item.getQuantity())
                    .sum();
            model.addAttribute("cartCount", count);
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Trang chủ                                                           */
    /* ------------------------------------------------------------------ */
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        // 3 sản phẩm mỗi category cho section sản phẩm ở index
        List<Product> all = productRepository.findByStatus("active");
        model.addAttribute("phonesPreview",     all.stream().filter(p -> "phones".equals(p.getCategory().getSlug())).limit(3).collect(Collectors.toList()));
        model.addAttribute("wearablesPreview",  all.stream().filter(p -> "wearables".equals(p.getCategory().getSlug())).limit(3).collect(Collectors.toList()));
        model.addAttribute("accessoriesPreview",all.stream().filter(p -> "accessories".equals(p.getCategory().getSlug())).limit(3).collect(Collectors.toList()));

        addCartCount(loggedInUser, model);
        return "index";
    }

    /* ------------------------------------------------------------------ */
    /*  Search API  GET /api/search?q=...  — trả về JSON tối đa 3 kết quả */
    /* ------------------------------------------------------------------ */
    @GetMapping("/api/search")
    @ResponseBody
    public List<Map<String, Object>> searchProducts(@RequestParam String q) {
        if (q == null || q.trim().length() < 1) return List.of();

        String keyword = q.trim().toLowerCase();

        // Fuzzy score: đếm số ký tự của keyword xuất hiện theo thứ tự trong chuỗi target
        java.util.function.BiFunction<String, String, Integer> fuzzyScore = (target, kw) -> {
            int score = 0, ti = 0;
            String t = target.toLowerCase();
            for (int ki = 0; ki < kw.length() && ti < t.length(); ki++) {
                while (ti < t.length() && t.charAt(ti) != kw.charAt(ki)) ti++;
                if (ti < t.length()) { score++; ti++; }
            }
            // Bonus nếu target chứa keyword nguyên chuỗi
            if (t.contains(kw)) score += kw.length() * 2;
            return score;
        };

        int minScore = Math.max(1, keyword.length() / 2);

        return productRepository.findByStatus("active").stream()
                .map(p -> {
                    String searchText = p.getName()
                            + " " + (p.getSpec() != null ? p.getSpec() : "")
                            + " " + p.getCategory().getName();
                    int score = fuzzyScore.apply(searchText, keyword);
                    return new Object[]{ p, score };
                })
                .filter(pair -> (int) pair[1] >= minScore)
                .sorted((a, b) -> Integer.compare((int) b[1], (int) a[1]))
                .limit(10)
                .map(pair -> {
                    Product p = (Product) pair[0];
                    Map<String, Object> item = new java.util.HashMap<>();
                    item.put("id",          p.getId());
                    item.put("name",        p.getName());
                    item.put("slug",        p.getSlug());
                    item.put("price",       p.getPrice());
                    item.put("thumbnailBg", p.getThumbnailBg() != null ? p.getThumbnailBg() : "");
                    item.put("category",    p.getCategory().getName());
                    return item;
                })
                .collect(Collectors.toList());
    }

    /* ------------------------------------------------------------------ */
    /*  Sản phẩm                                                            */
    /* ------------------------------------------------------------------ */
    @GetMapping("/products")
    public String products(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Product> activeProducts = productRepository.findByStatus("active");
        model.addAttribute("products", activeProducts);
        model.addAttribute("loggedInUser", loggedInUser);
        addCartCount(loggedInUser, model);
        return "products";
    }

    /* ------------------------------------------------------------------ */
    /*  Hỗ trợ                                                              */
    /* ------------------------------------------------------------------ */
    @GetMapping("/support")
    public String support(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        addCartCount(loggedInUser, model);
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
        addCartCount(loggedInUser, model);
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
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("errorMsg", e.getMessage());
            addCartCount(loggedInUser, model);
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
            addCartCount(loggedInUser, model);
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
            addCartCount(loggedInUser, model);
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
            addCartCount(loggedInUser, model);
            return "user";
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Xoá thông tin thanh toán                                           */
    /* ------------------------------------------------------------------ */
    @PostMapping("/user/delete-payment")
    public String deletePayment(@RequestParam Integer id,
                                @RequestParam String type,
                                HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        if ("card".equals(type))      paymentService.deleteCard(id, loggedInUser);
        else if ("bank".equals(type)) paymentService.deleteBank(id, loggedInUser);
        return "redirect:/user?tab=payment";
    }
}