package org.example.bttuan6.repository;

import org.example.bttuan6.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Lấy các booking của 1 tour
    List<Booking> findByTourId(Long tourId);
    List<Booking> findByScheduleId(Long scheduleId);
}
