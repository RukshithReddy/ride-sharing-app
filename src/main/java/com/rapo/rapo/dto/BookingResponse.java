package com.rapo.rapo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long bookingId;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private String driverName;
}