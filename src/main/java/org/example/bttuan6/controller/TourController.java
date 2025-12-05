package org.example.bttuan6.controller;


import com.bttuan6.entity.Tour;
import com.bttuan6.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    @Autowired
    private TourService tourService;

    // Lấy danh sách tour
    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    // API lọc tour
    @GetMapping("/search")
    public List<Tour> searchTours(@RequestParam(required = false) String location,
                                  @RequestParam(required = false) Double maxPrice) {
        return tourService.filterTours(location, maxPrice);
    }

    // Admin: Thêm tour mới
    @PostMapping("/admin/create")
    public Tour createTour(@RequestBody Tour tour) {
        return tourService.saveTour(tour);
    }
}
