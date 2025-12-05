package org.example.bttuan6.dto;

public class BookingResponse {
    private Long bookingId;
    private String tourName;
    private Double totalPrice; // Hoặc dùng BigDecimal tùy logic bên Service
    private String message;

    public BookingResponse() {
    }

    // --- GETTERS VÀ SETTERS (Phần bạn đang thiếu) ---

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}