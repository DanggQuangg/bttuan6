package org.example.bttuan6.controller;

import org.example.bttuan6.entity.Booking;
import org.example.bttuan6.entity.Tour;
import org.example.bttuan6.repository.TourRepository;
import org.example.bttuan6.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/tours")   // 👈 TẤT CẢ ROUTE ADMIN ĐỀU CHẠY DƯỚI /admin/tours
public class TourController {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourService tourService;

    // ======= DANH SÁCH TOUR (TRANG ADMIN) =======
    @GetMapping
    public String listTours(Model model) {
        List<Tour> tours = tourRepository.findAll();
        model.addAttribute("tours", tours);
        return "tour/list";   // => templates/tour/list.html
    }

    // ======= FORM THÊM TOUR MỚI =======
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Tour tour = new Tour();
        model.addAttribute("tour", tour);
        model.addAttribute("formTitle", "Thêm tour mới");
        return "tour/form";   // => templates/tour/form.html
    }

    // ======= FORM SỬA TOUR =======
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tour với id: " + id));

        model.addAttribute("tour", tour);
        model.addAttribute("formTitle", "Chỉnh sửa tour");
        return "tour/form";
    }

    // ======= LƯU TOUR (CẢ THÊM MỚI + CẬP NHẬT) =======
    @PostMapping("/save")
    public String saveTour(@ModelAttribute("tour") Tour tour) {

        if (tour.getId() == null) {
            // tạo mới
            tour.setCreatedAt(LocalDateTime.now());
        } else {
            // cập nhật: giữ nguyên createdAt cũ
            Tour old = tourRepository.findById(tour.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tour với id: " + tour.getId()));
            tour.setCreatedAt(old.getCreatedAt());
        }
        tour.setUpdatedAt(LocalDateTime.now());

        tourRepository.save(tour);

        return "redirect:/admin/tours";   // 👈 quay về trang admin
    }

    // XÓA TOUR
    @GetMapping("/{id}/delete")
    public String deleteTour(@PathVariable("id") Long id) {
        tourRepository.deleteById(id);
        return "redirect:/admin/tours";   // 👈 quay về admin
    }

    // ======= XEM DANH SÁCH KHÁCH THEO TOUR =======
    @GetMapping("/{id}/customers")
    public String viewTourCustomers(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        if (tour == null) {
            return "redirect:/admin/tours";
        }

        List<Booking> bookings = tourService.getBookingsByTourId(id);

        model.addAttribute("tour", tour);
        model.addAttribute("bookings", bookings);

        return "tour/customers"; // => templates/tour/customers.html
    }
}
