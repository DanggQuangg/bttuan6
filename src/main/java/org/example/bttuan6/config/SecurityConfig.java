package org.example.bttuan6.config;

import org.example.bttuan6.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean mã hóa mật khẩu với BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Lấy thông tin user từ MySQL qua JPA
     */
    @Bean
    public UserDetailsService userDetailsService(AppUserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())     // ĐÃ là password mã hóa
                        .roles(u.getRole())            // "USER", "ADMIN" -> Spring sẽ tự thêm tiền tố "ROLE_"
                        .disabled(!u.isEnabled())
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Không tìm thấy user: " + username));
    }

    /**
     * Cấu hình AuthenticationManager để dùng userDetailsService + passwordEncoder
     */
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {

        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return auth.build();
    }

    /**
     * Cấu hình phân quyền + form login
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // demo nên tắt, nếu làm thật hãy cấu hình cẩn thận hơn
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // Chỉ ADMIN được phép vào phần admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Các request còn lại cần đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")          // templates/login.html
                        .loginProcessingUrl("/login") // action="post" tới /login
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
