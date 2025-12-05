package org.example.bttuan6.service;


import org.example.bttuan6.entity.Tour;
import org.example.bttuan6.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    public List<Tour> filterTours(String location, Double maxPrice) {
        if (location != null) return tourRepository.findByLocationContainingIgnoreCase(location);
        if (maxPrice != null) return tourRepository.findByPriceLessThan(maxPrice);
        return tourRepository.findAll();
    }
}
