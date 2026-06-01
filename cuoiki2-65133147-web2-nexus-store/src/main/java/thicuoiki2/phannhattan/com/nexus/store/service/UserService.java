package thicuoiki2.phannhattan.com.nexus.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Khởi tạo bộ mã hóa mật khẩu chuẩn bảo mật cao BCrypt
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Đăng ký tài khoản người dùng mới
     */
    public User registerNewUser(User user) throws Exception {
        // 1. Kiểm tra Email trùng lặp
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email này đã được sử dụng bởi một tài khoản khác!");
        }

        // 2. Tiến hành băm mật khẩu (Hash) để lưu trữ an toàn trong DB
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        // 3. Đặt quyền mặc định nếu chưa có
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("customer");
        }

        // 4. Lưu thực thể người dùng mới xuống database
        return userRepository.save(user);
    }

    /**
     * Xác thực thông tin đăng nhập người dùng
     */
    public User authenticateUser(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Tài khoản email này không tồn tại!"));

        // So khớp mật khẩu thuần người dùng nhập với chuỗi băm trong DB
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new Exception("Mật khẩu đăng nhập không chính xác!");
        }

        return user;
    }
}