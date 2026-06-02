package thicuoiki2.phannhattan.com.nexus.store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thicuoiki2.phannhattan.com.nexus.store.entity.Category;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.repository.CategoryRepository;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;
import thicuoiki2.phannhattan.com.nexus.store.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    private boolean isAdmin(User user) {
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }

    @GetMapping("/admin")
    public String adminPage(
            @RequestParam(required = false) Integer editUserId,
            @RequestParam(required = false) Integer editProductId,
            @RequestParam(required = false) String userQuery,
            @RequestParam(required = false) String userRole,
            @RequestParam(required = false) String productQuery,
            @RequestParam(required = false) String productStatus,
            @RequestParam(required = false) Integer productCategoryId,
            HttpSession session,
            Model model) {

        User admin = getLoggedInUser(session);
        if (!isAdmin(admin)) {
            return "redirect:/login";
        }

        model.addAttribute("loggedInUser", admin);
        model.addAttribute("categories", categoryRepository.findAll());

        List<User> users = userRepository.findAll();
        if (userQuery != null && !userQuery.isBlank()) {
            String query = userQuery.trim().toLowerCase();
            users.removeIf(user -> {
                String fullName = ((user.getFirstName() == null ? "" : user.getFirstName()) + " " + (user.getLastName() == null ? "" : user.getLastName())).toLowerCase();
                String email = user.getEmail() == null ? "" : user.getEmail().toLowerCase();
                String phone = user.getPhone() == null ? "" : user.getPhone().toLowerCase();
                return !fullName.contains(query) && !email.contains(query) && !phone.contains(query);
            });
        }
        if (userRole != null && !userRole.isBlank()) {
            users.removeIf(user -> user.getRole() == null || !user.getRole().equalsIgnoreCase(userRole));
        }

        List<Product> products = productRepository.findAll();
        if (productQuery != null && !productQuery.isBlank()) {
            String query = productQuery.trim().toLowerCase();
            products.removeIf(product -> {
                String name = product.getName() == null ? "" : product.getName().toLowerCase();
                String slug = product.getSlug() == null ? "" : product.getSlug().toLowerCase();
                String spec = product.getSpec() == null ? "" : product.getSpec().toLowerCase();
                return !name.contains(query) && !slug.contains(query) && !spec.contains(query);
            });
        }
        if (productStatus != null && !productStatus.isBlank()) {
            products.removeIf(product -> product.getStatus() == null || !product.getStatus().equalsIgnoreCase(productStatus));
        }
        if (productCategoryId != null) {
            products.removeIf(product -> product.getCategory() == null || !product.getCategory().getId().equals(productCategoryId));
        }

        model.addAttribute("users", users);
        model.addAttribute("products", products);
        model.addAttribute("userQuery", userQuery);
        model.addAttribute("userRole", userRole);
        model.addAttribute("productQuery", productQuery);
        model.addAttribute("productStatus", productStatus);
        model.addAttribute("productCategoryId", productCategoryId);

        if (editUserId != null) {
            userRepository.findById(editUserId).ifPresent(u -> model.addAttribute("editUser", u));
        }

        if (editProductId != null) {
            productRepository.findById(editProductId).ifPresent(p -> model.addAttribute("editProduct", p));
        }

        return "admin";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(
            @RequestParam(required = false) Integer id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam String role,
            @RequestParam(required = false) String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User admin = getLoggedInUser(session);
        if (!isAdmin(admin)) {
            return "redirect:/login";
        }

        try {
            User user;

            if (id != null) {
                user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại."));
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setRole(role);
                if (password != null && !password.trim().isEmpty()) {
                    user.setPasswordHash(passwordEncoder.encode(password));
                }
            } else {
                user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setRole(role == null || role.isBlank() ? "customer" : role);
                String initialPassword = (password == null || password.isBlank()) ? "123456" : password;
                user.setPasswordHash(passwordEncoder.encode(initialPassword));
            }

            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMsg", "Đã lưu thông tin user thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }

        return "redirect:/admin#users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam Integer id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User admin = getLoggedInUser(session);
        if (!isAdmin(admin)) {
            return "redirect:/login";
        }

        if (id != null) {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMsg", "Đã xóa user.");
        }

        return "redirect:/admin#users";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(
            @RequestParam(required = false) Integer id,
            @RequestParam String name,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) String spec,
            @RequestParam Double price,
            @RequestParam(required = false) String badge,
            @RequestParam(required = false) String thumbnailBg,
            @RequestParam(required = false) String status,
            @RequestParam Integer categoryId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User admin = getLoggedInUser(session);
        if (!isAdmin(admin)) {
            return "redirect:/login";
        }

        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Danh mục không hợp lệ."));

            String normalizedSlug = (slug == null || slug.isBlank())
                    ? generateSlug(name)
                    : generateSlug(slug);

            Optional<Product> duplicateSlug = productRepository.findBySlug(normalizedSlug);
            if (duplicateSlug.isPresent() && (id == null || !duplicateSlug.get().getId().equals(id))) {
                throw new RuntimeException("Slug sản phẩm đã tồn tại.");
            }

            Product product;
            if (id != null) {
                product = productRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại."));
            } else {
                product = new Product();
            }

            product.setName(name);
            product.setSlug(normalizedSlug);
            product.setSpec(spec);
            product.setPrice(price);
            product.setBadge(badge);
            product.setThumbnailBg(thumbnailBg);
            product.setStatus(status == null || status.isBlank() ? "active" : status);
            product.setCategory(category);

            productRepository.save(product);
            redirectAttributes.addFlashAttribute("successMsg", "Đã lưu thông tin sản phẩm thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }

        return "redirect:/admin#products";
    }

    @PostMapping("/admin/products/delete")
    public String deleteProduct(@RequestParam Integer id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User admin = getLoggedInUser(session);
        if (!isAdmin(admin)) {
            return "redirect:/login";
        }

        if (id != null) {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMsg", "Đã xóa sản phẩm.");
        }

        return "redirect:/admin#products";
    }

    private String generateSlug(String source) {
        return source == null ? "" : source.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
