package org.example.bttuan6.controller;


import org.example.bttuan6.dto.BookingRequest;
import org.example.bttuan6.dto.BookingResponse;
import org.example.bttuan6.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<?> bookTour(@RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.bookTour(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
