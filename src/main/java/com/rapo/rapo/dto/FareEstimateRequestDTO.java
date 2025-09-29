package com.rapo.rapo.dto;

import lombok.Data;

@Data
public class FareEstimateRequestDTO {
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
}