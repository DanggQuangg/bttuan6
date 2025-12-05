package org.example.bttuan6.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long scheduleId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Integer quantity;
}
