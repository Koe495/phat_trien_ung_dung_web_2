package thicuoiki2.phannhattan.com.nexus.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thicuoiki2.phannhattan.com.nexus.store.entity.*;
import thicuoiki2.phannhattan.com.nexus.store.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Order placeOrder(User user,
                            String lastName, String firstName,
                            String email, String phone,
                            String address, String paymentMethod,
                            List<CartItem> cartItems) {

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng!");
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setShippingName(lastName + " " + firstName);
        order.setShippingEmail(email);
        order.setShippingPhone(phone);
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);

        // Tạo order items từ cart items
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setColor(cartItem.getColor());
            oi.setQuantity(cartItem.getQuantity());
            oi.setPrice(cartItem.getProduct().getPrice());
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItems.add(oi);
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Gửi email xác nhận đặt hàng
        emailService.sendOrderConfirmation(savedOrder);

        return savedOrder;
    }
}