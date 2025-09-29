package com.rapo.rapo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RideSearchResponseDTO {
    private Long id;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;
    private String driverName;
    private Double estimatedFare; // The new field for the fare
}