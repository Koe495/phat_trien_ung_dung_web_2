package thicuoiki2.phannhattan.com.nexus.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Cho phép truy cập không giới hạn vào mọi đường dẫn (Trang chủ, sản phẩm, login, register, tĩnh,...)
            // Điều này giúp bạn kiểm soát xác thực qua Session tùy ý mà không bị Spring Security chặn mặc định
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Tạm thời tắt tính năng chống giả mạo CSRF để bạn dễ dàng test form POST từ Vanilla HTML
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
            
        return http.build();
    }
}