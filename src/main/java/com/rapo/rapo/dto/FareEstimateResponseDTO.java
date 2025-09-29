package com.rapo.rapo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FareEstimateResponseDTO {
    private double distanceInKm;
    private double estimatedFare;
}