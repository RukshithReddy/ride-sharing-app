package com.rapo.rapo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RideRequest {
    @NotBlank
    private String source;
    @NotBlank
    private String destination;
    @NotNull @Future
    private LocalDateTime dateTime; 
    @NotNull @Min(1)
    private Integer availableSeats;
    @NotNull @Min(0)
    private Double fare; 
    
    
    // remove if it works good
    
    @NotNull
    private Double startLatitude;
    @NotNull
    private Double startLongitude;
    @NotNull
    private Double endLatitude;
    @NotNull
    private Double endLongitude;

}