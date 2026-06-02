package thicuoiki2.phannhattan.com.nexus.store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.service.AiChatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REST controller cho chatbot AI ở trang Hỗ trợ.
 * Lịch sử hội thoại được lưu trong HttpSession để bot trả lời theo ngữ cảnh.
 */
@RestController
public class SupportChatController {

    private final AiChatService aiChatService;

    /** Key lưu lịch sử hội thoại trong session. */
    private static final String SESSION_KEY = "supportChatHistory";

    /** Số message tối đa giữ lại trong lịch sử (tránh prompt quá dài/tốn token). */
    private static final int MAX_HISTORY = 12;

    /** Giới hạn độ dài câu hỏi để tránh lạm dụng. */
    private static final int MAX_MESSAGE_LENGTH = 1000;

    public SupportChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/api/support/chat")
    @SuppressWarnings("unchecked")
    public Map<String, Object> chat(@RequestBody Map<String, String> payload, HttpSession session) {

        String message = payload.getOrDefault("message", "");
        message = message == null ? "" : message.trim();

        if (message.isEmpty()) {
            return Map.of("reply", "Bạn vui lòng nhập câu hỏi nhé!");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            message = message.substring(0, MAX_MESSAGE_LENGTH);
        }

        // Lấy lịch sử hội thoại từ session
        List<Map<String, String>> history =
                (List<Map<String, String>>) session.getAttribute(SESSION_KEY);
        if (history == null) {
            history = new ArrayList<>();
        }

        // Lấy tên khách nếu đã đăng nhập
        User user = (User) session.getAttribute("loggedInUser");
        String customerName = null;
        if (user != null) {
            String last  = user.getLastName()  == null ? "" : user.getLastName();
            String first = user.getFirstName() == null ? "" : user.getFirstName();
            customerName = (last + " " + first).trim();
        }

        // Gọi AI
        String reply = aiChatService.chat(history, message, customerName);

        // Cập nhật lịch sử
        history.add(Map.of("role", "user", "content", message));
        history.add(Map.of("role", "assistant", "content", reply));

        // Giữ lại MAX_HISTORY message gần nhất
        if (history.size() > MAX_HISTORY) {
            history = new ArrayList<>(history.subList(history.size() - MAX_HISTORY, history.size()));
        }
        session.setAttribute(SESSION_KEY, history);

        return Map.of("reply", reply);
    }

    /** Xóa lịch sử hội thoại (bắt đầu cuộc trò chuyện mới). */
    @PostMapping("/api/support/chat/reset")
    public Map<String, Object> reset(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
        return Map.of("status", "ok");
    }
}
