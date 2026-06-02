package thicuoiki2.phannhattan.com.nexus.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thicuoiki2.phannhattan.com.nexus.store.entity.*;
import thicuoiki2.phannhattan.com.nexus.store.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired private CartItemRepository    cartItemRepository;
    @Autowired private ProductRepository     productRepository;
    @Autowired private ProductColorRepository colorRepository;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public double getSubtotal(List<CartItem> items) {
        return items.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();
    }

    /**
     * Thêm vào giỏ.
     * Gộp chỉ khi cùng productId VÀ cùng colorId — khác màu = item riêng.
     */
    @Transactional
    public void addToCart(User user, Integer productId, Integer colorId, int quantity) {
        // Tìm item đã có với chính xác cùng product + color
        Optional<CartItem> existing = cartItemRepository
                .findByUserAndProductIdAndColorId(user, productId, colorId);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(quantity);

            if (colorId != null) {
                colorRepository.findById(colorId).ifPresent(item::setColor);
            }

            cartItemRepository.save(item);
        }
    }

    @Transactional
    public void updateQuantity(User user, Integer itemId, int quantity) {
        cartItemRepository.findById(itemId).ifPresent(item -> {
            if (item.getUser().getId().equals(user.getId())) {
                if (quantity <= 0) cartItemRepository.delete(item);
                else { item.setQuantity(quantity); cartItemRepository.save(item); }
            }
        });
    }

    @Transactional
    public void removeItem(User user, Integer itemId) {
        cartItemRepository.findById(itemId).ifPresent(item -> {
            if (item.getUser().getId().equals(user.getId()))
                cartItemRepository.delete(item);
        });
    }

    /** Xoá toàn bộ giỏ hàng */
    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }

    /**
     * Xoá chỉ những item đã thanh toán (selectedIds).
     * Dùng sau khi đặt hàng thành công với lựa chọn item.
     */
    @Transactional
    public void removeSelectedItems(User user, List<Integer> selectedIds) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            clearCart(user);
            return;
        }
        List<CartItem> toRemove = cartItemRepository.findByUser(user).stream()
                .filter(item -> selectedIds.contains(item.getId()))
                .collect(Collectors.toList());
        cartItemRepository.deleteAll(toRemove);
    }

    /** Lấy danh sách item theo selectedIds (để tính tổng tiền checkout) */
    public List<CartItem> getSelectedItems(User user, List<Integer> selectedIds) {
        if (selectedIds == null || selectedIds.isEmpty())
            return getCartItems(user);
        return cartItemRepository.findByUser(user).stream()
                .filter(item -> selectedIds.contains(item.getId()))
                .collect(Collectors.toList());
    }
}