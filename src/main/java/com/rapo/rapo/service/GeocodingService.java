package com.rapo.rapo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingService {

    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1";

    public double[] getCoordinates(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String url = String.format(NOMINATIM_API_URL, encodedAddress);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (root.isArray() && !root.isEmpty()) {
                JsonNode location = root.get(0);
                double lat = location.path("lat").asDouble();
                double lon = location.path("lon").asDouble();
                return new double[]{lat, lon};
            } else {
                // Return a default or throw a more specific exception
                throw new RuntimeException("Could not find coordinates for the address: " + address);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during geocoding", e);
        }
    }
}