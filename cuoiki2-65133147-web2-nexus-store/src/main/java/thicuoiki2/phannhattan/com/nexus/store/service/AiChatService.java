package thicuoiki2.phannhattan.com.nexus.store.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import thicuoiki2.phannhattan.com.nexus.store.entity.Product;
import thicuoiki2.phannhattan.com.nexus.store.repository.ProductRepository;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Dịch vụ gọi API mô hình ngôn ngữ lớn (LLM) của Groq để trả lời câu hỏi
 * của khách hàng trên trang Hỗ trợ.
 *
 * Groq cung cấp endpoint tương thích OpenAI:
 *   POST https://api.groq.com/openai/v1/chat/completions
 */
@Service
public class AiChatService {

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${groq.api.model:llama-3.3-70b-versatile}")
    private String model;

    private final ProductRepository productRepository;
    private final RestClient restClient = RestClient.create();

    private static final NumberFormat VND = NumberFormat.getInstance(new Locale("vi", "VN"));

    public AiChatService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** Kiểm tra đã cấu hình API key hay chưa. */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Gửi câu hỏi của người dùng kèm lịch sử hội thoại tới Groq và trả về câu trả lời.
     *
     * @param history      lịch sử hội thoại trước đó (mỗi phần tử có "role" và "content")
     * @param userMessage  câu hỏi hiện tại của người dùng
     * @param customerName tên khách hàng nếu đã đăng nhập (có thể null)
     */
    @SuppressWarnings("unchecked")
    public String chat(List<Map<String, String>> history, String userMessage, String customerName) {

        if (!isConfigured()) {
            return "Xin lỗi, trợ lý ảo hiện chưa được kích hoạt. "
                 + "Vui lòng liên hệ hotline 1800 1234 để được hỗ trợ trực tiếp.";
        }

        // Xây dựng danh sách messages theo chuẩn OpenAI/Groq
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", buildSystemPrompt(customerName)));
        if (history != null) {
            messages.addAll(history);
        }
        messages.add(Map.of("role", "user", "content", userMessage));

        // Body của request
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("temperature", 0.5);
        body.put("max_tokens", 800);

        try {
            Map<String, Object> response = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return fallbackMessage();
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                return fallbackMessage();
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = message != null ? (String) message.get("content") : null;

            return (content == null || content.isBlank()) ? fallbackMessage() : content.trim();

        } catch (Exception e) {
            System.err.println("[AiChatService] Lỗi gọi Groq API: " + e.getMessage());
            return fallbackMessage();
        }
    }

    private String fallbackMessage() {
        return "Xin lỗi, trợ lý ảo đang gặp sự cố tạm thời. "
             + "Bạn vui lòng thử lại sau ít phút hoặc gọi hotline 1800 1234 (8h–21h hằng ngày).";
    }

    /**
     * Xây dựng "system prompt" — nhồi ngữ cảnh về Nexus Store + danh mục sản phẩm
     * để mô hình trả lời chính xác, đúng phạm vi (RAG đơn giản qua prompt).
     */
    private String buildSystemPrompt(String customerName) {
        StringBuilder sb = new StringBuilder();

        sb.append("Bạn là \"Trợ lý Nexus\" — trợ lý ảo chăm sóc khách hàng của cửa hàng công nghệ Nexus Store ")
          .append("(chuyên điện thoại, thiết bị đeo và phụ kiện).\n\n");

        sb.append("QUY TẮC TRẢ LỜI:\n")
          .append("- Luôn trả lời bằng tiếng Việt, thân thiện, ngắn gọn, rõ ràng.\n")
          .append("- Chỉ tư vấn các vấn đề liên quan đến Nexus Store: sản phẩm, giá, đặt hàng, ")
          .append("thanh toán, vận chuyển, bảo hành, đổi trả, tài khoản. Lịch sự từ chối các câu hỏi ngoài phạm vi.\n")
          .append("- Nếu không chắc chắn hoặc cần thao tác trên đơn hàng cụ thể, hãy hướng khách liên hệ hotline 1800 1234.\n")
          .append("- Không bịa đặt thông tin sản phẩm hay chính sách không có trong dữ liệu dưới đây.\n")
          .append("- Có thể trả lời tối đa vài câu, dùng gạch đầu dòng khi cần liệt kê.\n\n");

        sb.append("CHÍNH SÁCH CỬA HÀNG:\n")
          .append("- Bảo hành chính hãng 12 tháng cho thiết bị, 6 tháng cho phụ kiện.\n")
          .append("- Đổi trả miễn phí trong 14 ngày nếu lỗi phần cứng từ nhà sản xuất (kèm hóa đơn, hộp, phụ kiện).\n")
          .append("- Thay pin miễn phí nếu còn bảo hành NEXUS Care+ và dung lượng pin tối đa dưới 80%; ")
          .append("ngoài bảo hành chi phí khoảng 1.000.000đ–2.500.000đ tùy dòng máy.\n")
          .append("- Hotline 1800 1234 (miễn phí, 8h–21h hằng ngày). Hỗ trợ chuyển dữ liệu qua ứng dụng \"Nexus Transfer\".\n")
          .append("- Phương thức thanh toán: COD, thẻ ngân hàng, chuyển khoản.\n\n");

        // Danh mục sản phẩm đang bán (RAG-lite)
        sb.append("DANH SÁCH SẢN PHẨM ĐANG BÁN:\n");
        try {
            List<Product> products = productRepository.findByStatus("active");
            int limit = Math.min(products.size(), 60);
            for (int i = 0; i < limit; i++) {
                Product p = products.get(i);
                String category = p.getCategory() != null ? p.getCategory().getName() : "Khác";
                sb.append("- ").append(p.getName())
                  .append(" | ").append(category)
                  .append(" | ").append(VND.format(p.getPrice())).append(" ₫");
                if (p.getSpec() != null && !p.getSpec().isBlank()) {
                    String spec = p.getSpec().length() > 80 ? p.getSpec().substring(0, 80) + "…" : p.getSpec();
                    sb.append(" | ").append(spec);
                }
                sb.append("\n");
            }
            if (products.isEmpty()) {
                sb.append("(Hiện chưa có sản phẩm nào đang mở bán.)\n");
            }
        } catch (Exception e) {
            sb.append("(Không tải được danh sách sản phẩm.)\n");
        }

        if (customerName != null && !customerName.isBlank()) {
            sb.append("\nKhách hàng đang đăng nhập có tên: ").append(customerName)
              .append(". Hãy xưng hô lịch sự với khách.\n");
        }

        return sb.toString();
    }
}
