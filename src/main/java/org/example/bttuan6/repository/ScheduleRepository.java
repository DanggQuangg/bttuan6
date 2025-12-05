package org.example.bttuan6.repository;

import org.example.bttuan6.entity.Schedule;
import org.example.bttuan6.entity.Tour;
import org.example.bttuan6.model.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTour(Tour tour);

    List<Schedule> findByTourId(Long tourId);

    List<Schedule> findByTourIdAndStatus(Long tourId, ScheduleStatus status);

    List<Schedule> findByDepartureDate(LocalDate departureDate);
}

