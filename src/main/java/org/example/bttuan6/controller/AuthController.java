package org.example.bttuan6.controller;

import org.example.bttuan6.entity.AppUser;
import org.example.bttuan6.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        // templates/login.html đã có form POST /login
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {
        // Check trùng username
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username đã tồn tại!");
            return "register";
        }

        // Check xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu nhập lại không khớp!");
            return "register";
        }

        // Mã hóa mật khẩu bằng BCrypt
        String encodedPassword = passwordEncoder.encode(password);

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("USER");     // mặc định role USER, nếu muốn admin thì sửa trong DB
        user.setEnabled(true);

        userRepository.save(user);

        // Đăng ký xong chuyển đến trang login
        return "redirect:/login?registered";
    }
}
