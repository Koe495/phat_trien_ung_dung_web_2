package thicuoiki2.phannhattan.com.nexus.store.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import thicuoiki2.phannhattan.com.nexus.store.entity.Order;
import thicuoiki2.phannhattan.com.nexus.store.entity.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromAddress;

    private static final NumberFormat VND = NumberFormat.getInstance(new Locale("vi", "VN"));

    // ──────────────────────────────────────────────────────────────────────────
    // Gửi email xác nhận đặt hàng thành công
    // ──────────────────────────────────────────────────────────────────────────
    @Async
    public void sendOrderConfirmation(Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(order.getShippingEmail());
            helper.setSubject("Nexus Store – Xác nhận đơn hàng #" + order.getId());
            helper.setText(buildOrderConfirmationHtml(order), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("[EmailService] Lỗi gửi email xác nhận đơn hàng #"
                    + order.getId() + ": " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Gửi email thông báo đơn hàng hoàn tất (completed)
    // ──────────────────────────────────────────────────────────────────────────
    @Async
    public void sendOrderCompleted(Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(order.getShippingEmail());
            helper.setSubject("Nexus Store – Đơn hàng #" + order.getId() + " đã hoàn tất");
            helper.setText(buildOrderCompletedHtml(order), true);

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("[EmailService] Lỗi gửi email hoàn tất đơn hàng #"
                    + order.getId() + ": " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HTML: xác nhận đặt hàng
    // ──────────────────────────────────────────────────────────────────────────
    private String buildOrderConfirmationHtml(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(emailHeader());
        sb.append("<h2 style='color:#2563eb;'>Xác nhận đơn hàng thành công!</h2>");
        sb.append("<p>Xin chào <strong>").append(order.getShippingName()).append("</strong>,</p>");
        sb.append("<p>Cảm ơn bạn đã đặt hàng tại <strong>Nexus Store</strong>. ");
        sb.append("Đơn hàng của bạn đã được ghi nhận và đang được xử lý.</p>");
        sb.append(orderSummaryTable(order));
        sb.append("<p><strong>Địa chỉ giao hàng:</strong> ").append(order.getShippingAddress()).append("</p>");
        sb.append("<p><strong>Phương thức thanh toán:</strong> ").append(order.getPaymentMethod()).append("</p>");
        sb.append("<p>Chúng tôi sẽ thông báo khi đơn hàng được giao. Cảm ơn bạn!</p>");
        sb.append(emailFooter());
        return sb.toString();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HTML: đơn hàng hoàn tất
    // ──────────────────────────────────────────────────────────────────────────
    private String buildOrderCompletedHtml(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(emailHeader());
        sb.append("<h2 style='color:#16a34a;'>Đơn hàng đã hoàn tất!</h2>");
        sb.append("<p>Xin chào <strong>").append(order.getShippingName()).append("</strong>,</p>");
        sb.append("<p>Đơn hàng <strong>#").append(order.getId())
          .append("</strong> của bạn đã được giao thành công và chuyển sang trạng thái <strong>Hoàn tất</strong>.</p>");
        sb.append(orderSummaryTable(order));
        sb.append("<p>Cảm ơn bạn đã mua sắm tại <strong>Nexus Store</strong>. ");
        sb.append("Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi.</p>");
        sb.append(emailFooter());
        return sb.toString();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Bảng tóm tắt sản phẩm
    // ──────────────────────────────────────────────────────────────────────────
    private String orderSummaryTable(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Chi tiết đơn hàng #").append(order.getId()).append("</h3>");
        sb.append("<table style='width:100%;border-collapse:collapse;font-size:14px;'>");
        sb.append("<thead><tr style='background:#f3f4f6;'>");
        sb.append("<th style='padding:8px;border:1px solid #e5e7eb;text-align:left;'>Sản phẩm</th>");
        sb.append("<th style='padding:8px;border:1px solid #e5e7eb;text-align:center;'>Màu</th>");
        sb.append("<th style='padding:8px;border:1px solid #e5e7eb;text-align:center;'>SL</th>");
        sb.append("<th style='padding:8px;border:1px solid #e5e7eb;text-align:right;'>Đơn giá</th>");
        sb.append("<th style='padding:8px;border:1px solid #e5e7eb;text-align:right;'>Thành tiền</th>");
        sb.append("</tr></thead><tbody>");

        List<OrderItem> items = order.getItems();
        if (items != null) {
            for (OrderItem item : items) {
                String productName = item.getProduct() != null ? item.getProduct().getName() : "";
                String colorName   = item.getColor()   != null ? item.getColor().getColorName()   : "–";
                String unitPrice   = VND.format(item.getPrice()) + " ₫";
                String subtotal    = VND.format(item.getPrice() * item.getQuantity()) + " ₫";
                sb.append("<tr>");
                sb.append("<td style='padding:8px;border:1px solid #e5e7eb;'>").append(productName).append("</td>");
                sb.append("<td style='padding:8px;border:1px solid #e5e7eb;text-align:center;'>").append(colorName).append("</td>");
                sb.append("<td style='padding:8px;border:1px solid #e5e7eb;text-align:center;'>").append(item.getQuantity()).append("</td>");
                sb.append("<td style='padding:8px;border:1px solid #e5e7eb;text-align:right;'>").append(unitPrice).append("</td>");
                sb.append("<td style='padding:8px;border:1px solid #e5e7eb;text-align:right;'>").append(subtotal).append("</td>");
                sb.append("</tr>");
            }
        }

        sb.append("<tr style='background:#f9fafb;font-weight:bold;'>");
        sb.append("<td colspan='4' style='padding:8px;border:1px solid #e5e7eb;text-align:right;'>Tổng cộng</td>");
        sb.append("<td style='padding:8px;border:1px solid #e5e7eb;text-align:right;color:#2563eb;'>")
          .append(VND.format(order.getTotalAmount())).append(" ₫</td>");
        sb.append("</tr>");
        sb.append("</tbody></table>");
        return sb.toString();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Header / Footer chung
    // ──────────────────────────────────────────────────────────────────────────
    private String emailHeader() {
        return "<div style='font-family:Arial,sans-serif;max-width:640px;margin:auto;padding:24px;'>"
             + "<div style='background:#2563eb;padding:16px 24px;border-radius:8px 8px 0 0;'>"
             + "<h1 style='color:#fff;margin:0;font-size:22px;'>Nexus Store</h1>"
             + "</div>"
             + "<div style='border:1px solid #e5e7eb;border-top:none;padding:24px;border-radius:0 0 8px 8px;'>";
    }

    private String emailFooter() {
        return "<hr style='border:none;border-top:1px solid #e5e7eb;margin:24px 0;'/>"
             + "<p style='font-size:12px;color:#6b7280;'>Email này được gửi tự động từ hệ thống Nexus Store. "
             + "Vui lòng không trả lời email này.</p>"
             + "</div></div>";
    }
}
