package org.example.bttuan6.repository;

import org.example.bttuan6.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    // Tìm kiếm/Lọc theo địa điểm và giá
    List<Tour> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT t FROM Tour t WHERE t.price <= :maxPrice")
    List<Tour> findByPriceLessThan(Double maxPrice);
}
