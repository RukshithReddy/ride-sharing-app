package com.rapo.rapo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationSuggestionDTO {
    private String displayName;
    private double latitude;
    private double longitude;
}