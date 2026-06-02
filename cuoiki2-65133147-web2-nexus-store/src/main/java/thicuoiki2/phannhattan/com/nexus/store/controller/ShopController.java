package thicuoiki2.phannhattan.com.nexus.store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import thicuoiki2.phannhattan.com.nexus.store.entity.*;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;
import thicuoiki2.phannhattan.com.nexus.store.service.CartService;
import thicuoiki2.phannhattan.com.nexus.store.service.OrderService;
import thicuoiki2.phannhattan.com.nexus.store.service.PaymentService;

import java.util.List;

@Controller
public class ShopController {

    @Autowired private ProductRepository productRepository;
    @Autowired private CartService       cartService;
    @Autowired private OrderService      orderService;
    @Autowired private PaymentService    paymentService;

    /* Helper */
    private void addCartCount(User user, Model model) {
        if (user != null) {
            int count = cartService.getCartItems(user).stream()
                    .mapToInt(CartItem::getQuantity).sum();
            model.addAttribute("cartCount", count);
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Chi tiết sản phẩm  GET /product/{slug}                             */
    /* ------------------------------------------------------------------ */
    @GetMapping("/product/{slug}")
    public String productDetail(@PathVariable String slug,
                                HttpSession session, Model model) {
        Product product = productRepository.findBySlug(slug).orElse(null);
        if (product == null) return "redirect:/products";

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("product",      product);
        model.addAttribute("loggedInUser", loggedInUser);
        addCartCount(loggedInUser, model);
        return "product-detail";
    }

    /* ------------------------------------------------------------------ */
    /*  Giỏ hàng  GET /cart                                                */
    /* ------------------------------------------------------------------ */
    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        List<CartItem> items = cartService.getCartItems(loggedInUser);
        model.addAttribute("cartItems",    items);
        model.addAttribute("subtotal",     cartService.getSubtotal(items));
        model.addAttribute("loggedInUser", loggedInUser);
        addCartCount(loggedInUser, model);
        return "cart";
    }

    /* ------------------------------------------------------------------ */
    /*  Thêm vào giỏ  POST /cart/add                                       */
    /* ------------------------------------------------------------------ */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam(required = false) Integer colorId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        cartService.addToCart(loggedInUser, productId, colorId, quantity);
        return "redirect:/cart";
    }

    /* ------------------------------------------------------------------ */
    /*  Cập nhật số lượng  POST /cart/update                               */
    /* ------------------------------------------------------------------ */
    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Integer itemId,
                             @RequestParam Integer quantity,
                             HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        cartService.updateQuantity(loggedInUser, itemId, quantity);
        return "redirect:/cart";
    }

    /* ------------------------------------------------------------------ */
    /*  Xoá sản phẩm  POST /cart/remove                                    */
    /* ------------------------------------------------------------------ */
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Integer itemId,
                                 HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        cartService.removeItem(loggedInUser, itemId);
        return "redirect:/cart";
    }

    /* ------------------------------------------------------------------ */
    /*  Trang checkout  GET /checkout                                       */
    /*  Nhận selectedItems từ form GET của cart                            */
    /* ------------------------------------------------------------------ */
    @GetMapping("/checkout")
    public String checkout(@RequestParam(required = false) List<Integer> selectedItems,
                           HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        // Lấy đúng những item được chọn (hoặc tất cả nếu không có lựa chọn)
        List<CartItem> items = cartService.getSelectedItems(loggedInUser, selectedItems);
        if (items.isEmpty()) return "redirect:/cart";

        model.addAttribute("cartItems",     items);
        model.addAttribute("subtotal",      cartService.getSubtotal(items));
        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("savedCards",    paymentService.getCardsByUser(loggedInUser));
        model.addAttribute("savedBanks",    paymentService.getBanksByUser(loggedInUser));
        model.addAttribute("loggedInUser",  loggedInUser);
        return "checkout";
    }

    /* ------------------------------------------------------------------ */
    /*  Đặt hàng  POST /checkout/place-order                               */
    /* ------------------------------------------------------------------ */
    @PostMapping("/checkout/place-order")
    public String placeOrder(@RequestParam String lastName,
                             @RequestParam String firstName,
                             @RequestParam String email,
                             @RequestParam String phone,
                             @RequestParam String address,
                             @RequestParam String payment,
                             @RequestParam(required = false) List<Integer> selectedItems,
                             HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        try {
            // Chỉ đặt hàng những item đã chọn
            List<CartItem> items = cartService.getSelectedItems(loggedInUser, selectedItems);
            Order order = orderService.placeOrder(
                    loggedInUser, lastName, firstName,
                    email, phone, address, payment, items
            );

            // Xoá chỉ các item đã thanh toán, giữ lại phần còn lại
            cartService.removeSelectedItems(loggedInUser, selectedItems);

            model.addAttribute("orderSuccess", true);
            model.addAttribute("orderId",      order.getId());
            model.addAttribute("loggedInUser", loggedInUser);
            return "checkout";

        } catch (Exception e) {
            List<CartItem> items = cartService.getSelectedItems(loggedInUser, selectedItems);
            model.addAttribute("errorMsg",      e.getMessage());
            model.addAttribute("cartItems",     items);
            model.addAttribute("subtotal",      cartService.getSubtotal(items));
            model.addAttribute("selectedItems", selectedItems);
            model.addAttribute("savedCards",    paymentService.getCardsByUser(loggedInUser));
            model.addAttribute("savedBanks",    paymentService.getBanksByUser(loggedInUser));
            model.addAttribute("loggedInUser",  loggedInUser);
            return "checkout";
        }
    }
}