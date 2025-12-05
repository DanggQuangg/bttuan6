package org.example.bttuan6.dto;

import lombok.Data;

@Data
public class BookingResponse {
    private Long bookingId;
    private String tourName;
    private Double totalPrice;
    private String message;
}
