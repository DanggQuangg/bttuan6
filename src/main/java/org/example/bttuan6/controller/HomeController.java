package org.example.bttuan6.controller;

import org.example.bttuan6.service.TourService;
import org.example.bttuan6.service.BookingService; // Cần thêm method lấy danh sách booking trong service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private TourService tourService;

    // Trang chủ - Hiển thị danh sách Tour
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        return "index"; // templates/index.html
    }

    // Trang chi tiết Tour
    @GetMapping("/tour/{id}")
    public String tourDetail(@PathVariable Long id, Model model) {
        model.addAttribute("tour", tourService.getTourById(id));
        return "tour-detail"; // templates/tour-detail.html
    }

    // Trang Admin (Chỉ Admin mới vào được do config ở trên)
    @GetMapping("/admin")
    public String adminPage(Model model) {
        // Bạn cần viết thêm method getAllBookings() trong BookingService
        // model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin"; // templates/admin.html
    }
}