package com.rapo.rapo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapo.rapo.dto.LocationSuggestionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    private static final String NOMINATIM_SUGGEST_API_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=5";

    public List<LocationSuggestionDTO> getSuggestions(String query) {
        List<LocationSuggestionDTO> suggestions = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = String.format(NOMINATIM_SUGGEST_API_URL, encodedQuery);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (root.isArray()) {
                for (JsonNode location : root) {
                    String displayName = location.path("display_name").asText();
                    double lat = location.path("lat").asDouble();
                    double lon = location.path("lon").asDouble();
                    suggestions.add(new LocationSuggestionDTO(displayName, lat, lon));
                }
            }
        } catch (Exception e) {
            // It's good practice to log the error
            e.printStackTrace();
            // Return an empty list in case of an error
        }
        return suggestions;
    }
}