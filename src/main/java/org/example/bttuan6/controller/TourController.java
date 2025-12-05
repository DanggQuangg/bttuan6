package org.example.bttuan6.controller;

import org.example.bttuan6.entity.Tour;
import org.example.bttuan6.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {

    @Autowired
    private TourRepository tourRepository;

    // ======= DANH SÁCH TOUR =======
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

        return "redirect:/tours";
    }

    // (tuỳ chọn) XÓA TOUR
    @GetMapping("/{id}/delete")
    public String deleteTour(@PathVariable("id") Long id) {
        tourRepository.deleteById(id);
        return "redirect:/tours";
    }
}

