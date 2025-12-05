package org.example.bttuan6.service;

import org.example.bttuan6.entity.Booking;
import org.example.bttuan6.entity.Tour;
import org.example.bttuan6.repository.BookingRepository;
import org.example.bttuan6.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // ================== HÀM CŨ ==================

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour saveTour(Tour tour) {

        if (tour.getId() == null) {
            // tạo mới
            tour.setCreatedAt(LocalDateTime.now());
        } else {
            // cập nhật: giữ createdAt cũ
            Tour old = tourRepository.findById(tour.getId()).orElse(null);
            if (old != null) {
                tour.setCreatedAt(old.getCreatedAt());
            }
        }

        tour.setUpdatedAt(LocalDateTime.now());

        return tourRepository.save(tour);
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    // Lọc tour theo location và/hoặc max price
    public List<Tour> filterTours(String location, Double maxPrice) {

        // Cả location và price cùng tồn tại
        if (location != null && !location.isBlank() && maxPrice != null) {
            List<Tour> tours = tourRepository.findByLocationContainingIgnoreCase(location);
            return tours.stream()
                    .filter(t -> t.getPrice() != null &&
                            t.getPrice().doubleValue() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Chỉ location
        if (location != null && !location.isBlank()) {
            return tourRepository.findByLocationContainingIgnoreCase(location);
        }

        // Chỉ maxPrice
        if (maxPrice != null) {
            return tourRepository.findByPriceLessThan(maxPrice);
        }

        return tourRepository.findAll();
    }

    // ================== HÀM MỚI ==================

    // Tạo tour mới (API /admin/create)
    public Tour createTour(Tour tour) {
        tour.setId(null);
        return saveTour(tour);
    }

    // Cập nhật tour (API /admin/{id})
    public Tour updateTour(Long id, Tour req) {
        Optional<Tour> opt = tourRepository.findById(id);
        if (opt.isEmpty()) return null;

        Tour tour = opt.get();

        tour.setName(req.getName());
        tour.setImageUrl(req.getImageUrl());
        tour.setPrice(req.getPrice());
        tour.setLocation(req.getLocation());
        tour.setDurationDays(req.getDurationDays());
        tour.setMaxGuests(req.getMaxGuests());
        tour.setDescription(req.getDescription());

        tour.setUpdatedAt(LocalDateTime.now());

        return tourRepository.save(tour);
    }

    // Xoá tour (API /admin/{id})
    public boolean deleteTour(Long id) {
        if (!tourRepository.existsById(id)) return false;
        tourRepository.deleteById(id);
        return true;
    }

    public List<Booking> getBookingsByTourId(Long tourId) {
        return bookingRepository.findByTourId(tourId);
    }
}
