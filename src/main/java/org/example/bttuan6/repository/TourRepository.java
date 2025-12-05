package org.example.bttuan6.repository;

import org.example.bttuan6.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // Lọc tour theo giá, địa điểm, số ngày, v.v.
    List<Tour> findByLocationContainingIgnoreCase(String location);

    List<Tour> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Tour> findByDurationDays(Integer durationDays);
}

