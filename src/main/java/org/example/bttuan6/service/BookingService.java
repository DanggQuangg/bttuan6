package org.example.bttuan6.service;

import org.example.bttuan6.dto.BookingRequest;
import org.example.bttuan6.dto.BookingResponse;
import org.example.bttuan6.entity.*;
import org.example.bttuan6.model.BookingStatus;
import org.example.bttuan6.model.PaymentStatus;
import org.example.bttuan6.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmailService emailService;

    @Transactional
    public BookingResponse bookTour(BookingRequest request) {
        // 1. Lấy thông tin lịch trình
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Lịch trình không tồn tại với ID: " + request.getScheduleId()));

        Tour tour = schedule.getTour();

        // 2. Kiểm tra số lượng khách tối đa
        // Logic: Lấy max khách của LỊCH (Schedule) thay vì của Tour chung, vì mỗi ngày có thể khác nhau
        List<Booking> existingBookings = bookingRepository.findByScheduleId(schedule.getId());
        int currentPax = existingBookings.stream().mapToInt(Booking::getNumberOfPeople).sum();

        if (currentPax + request.getQuantity() > schedule.getMaxGuests()) {
            throw new RuntimeException("Lịch khởi hành này đã hết chỗ! Chỉ còn: " + (schedule.getMaxGuests() - currentPax) + " chỗ.");
        }

        // 3. Xử lý khách hàng (Tránh tạo trùng nếu email đã có)
        // Lưu ý: Cần thêm method findByEmail trong CustomerRepository
        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getEmail());

        Customer customer;
        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            // Có thể update lại thông tin mới nhất nếu muốn
            customer.setPhone(request.getPhone());
            customer.setAddress(request.getAddress());
            customerRepository.save(customer);
        } else {
            customer = new Customer();
            customer.setFullName(request.getFullName());
            customer.setEmail(request.getEmail());
            customer.setPhone(request.getPhone());
            customer.setAddress(request.getAddress());
            customer.setCreatedAt(LocalDateTime.now());
            customerRepository.save(customer);
        }

        // 4. Tạo Booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setTour(tour); // QUAN TRỌNG: Phải set Tour vì trong Entity tour_id nullable=false
        booking.setNumberOfPeople(request.getQuantity());
        booking.setBookingDate(LocalDateTime.now());

        // Sử dụng Enum thay vì String
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.UNPAID);

        // 5. Tính tiền (Xử lý BigDecimal)
        BigDecimal pricePerPerson = tour.getPrice();
        BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
        BigDecimal total = pricePerPerson.multiply(quantity); // Phép nhân trong BigDecimal

        booking.setTotalPrice(total);

        bookingRepository.save(booking);

        // 6. Gửi email xác nhận
        // (Bọc try-catch để lỗi gửi mail không làm rollback transaction đặt tour)
        try {
            emailService.sendBookingConfirmation(
                    customer.getEmail(),
                    "Xác nhận đặt tour: " + tour.getName(),
                    "Cảm ơn bạn đã đặt tour. Tổng tiền: " + total + " VNĐ"
            );
        } catch (Exception e) {
            System.err.println("Không thể gửi email: " + e.getMessage());
        }

        // 7. Trả về kết quả
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setTourName(tour.getName());
        // Chuyển BigDecimal về Double để trả về DTO (hoặc sửa DTO dùng BigDecimal thì tốt hơn)
        response.setTotalPrice(total.doubleValue());
        response.setMessage("Đặt tour thành công!");

        return response;
    }
}