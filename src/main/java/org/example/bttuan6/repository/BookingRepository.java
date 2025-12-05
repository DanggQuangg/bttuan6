package org.example.bttuan6.repository;

import org.example.bttuan6.entity.Booking;
import org.example.bttuan6.model.BookingStatus;
import org.example.bttuan6.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Xem danh sách khách theo từng tour
    List<Booking> findByTourId(Long tourId);

    // Xem danh sách khách theo lịch khởi hành cụ thể
    List<Booking> findByScheduleId(Long scheduleId);

    // Lọc theo trạng thái
    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);
}

