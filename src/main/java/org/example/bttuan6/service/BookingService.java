package org.example.bttuan6.service;

import org.example.bttuan6.dto.BookingRequest;
import org.example.bttuan6.dto.BookingResponse;
import org.example.bttuan6.entity.*;
import org.example.bttuan6.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ScheduleRepository scheduleRepository; // Cần tạo repo này
    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmailService emailService;

    @Transactional
    public BookingResponse bookTour(BookingRequest request) {
        // 1. Lấy thông tin lịch trình
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Lịch trình không tồn tại"));
        Tour tour = schedule.getTour();

        // 2. Kiểm tra số lượng khách tối đa
        // (Logic đơn giản: đếm tổng khách đã đặt cho lịch này)
        List<Booking> existingBookings = bookingRepository.findByScheduleId(schedule.getId());
        int currentPax = existingBookings.stream().mapToInt(Booking::getQuantity).sum();

        if (currentPax + request.getQuantity() > tour.getMaxPeople()) {
            throw new RuntimeException("Tour đã hết chỗ hoặc không đủ số lượng ghế!");
        }

        // 3. Lưu thông tin khách hàng (nếu chưa có thì tạo mới, ở đây tạo mới luôn cho đơn giản)
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customerRepository.save(customer);

        // 4. Tạo Booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setQuantity(request.getQuantity());
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("PENDING"); // Mặc định chưa thanh toán

        // Tính tiền
        double total = tour.getPrice() * request.getQuantity();
        booking.setTotalPrice(total);

        bookingRepository.save(booking);

        // 5. Gửi email xác nhận
        emailService.sendBookingConfirmation(
                customer.getEmail(),
                "Xác nhận đặt tour: " + tour.getTitle(),
                "Cảm ơn bạn đã đặt tour. Tổng tiền: " + total
        );

        // 6. Trả về kết quả
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setTourName(tour.getTitle());
        response.setTotalPrice(total);
        response.setMessage("Đặt tour thành công!");

        return response;
    }
}
