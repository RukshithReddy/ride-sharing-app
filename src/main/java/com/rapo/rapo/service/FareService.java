package com.rapo.rapo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FareService {

    // You can move these to application.properties for configurability
    private static final double BASE_FARE = 50.0;
    private static final double RATE_PER_KM = 12.0;

    public double calculateFare(double distanceInKm) {
        return BASE_FARE + (RATE_PER_KM * distanceInKm);
    }

    public double getDistance(double startLat, double startLon, double endLat, double endLon) {
        // Using the public OSRM API
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?overview=false",
                startLon, startLat, endLon, endLat);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode routes = root.path("routes");
            if (routes.isArray() && !routes.isEmpty()) {
                double distanceInMeters = routes.get(0).path("distance").asDouble();
                return Math.round((distanceInMeters / 1000.0) * 100.0) / 100.0;
            }
        } catch (Exception e) {
            // Fallback to Haversine distance if OSRM is unavailable
            double haversineKm = haversineDistanceKm(startLat, startLon, endLat, endLon);
            return Math.round(haversineKm * 100.0) / 100.0;
        }
        // If no route is found, fallback to Haversine as well
        double haversineKm = haversineDistanceKm(startLat, startLon, endLat, endLon);
        return Math.round(haversineKm * 100.0) / 100.0;
    }

    private double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double earthRadiusKm = 6371.0088;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}