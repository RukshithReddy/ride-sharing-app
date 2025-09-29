//package com.rapo.rapo.controller;
//
//import com.rapo.rapo.dto.LocationSuggestionDTO;
//import com.rapo.rapo.service.LocationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/locations")
//public class LocationController {
//
//    @Autowired
//    private LocationService locationService;
//
//    @GetMapping("/autocomplete")
//    public ResponseEntity<List<LocationSuggestionDTO>> autocomplete(@RequestParam String query) {
//        return ResponseEntity.ok(locationService.getAutocompleteSuggestions(query));
//    }
//}



package com.rapo.rapo.controller;

import com.rapo.rapo.dto.LocationSuggestionDTO;
import com.rapo.rapo.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<LocationSuggestionDTO>> getSuggestions(@RequestParam String query) {
        if (query == null || query.trim().length() < 3) {
            // Don't search for very short queries to avoid excessive API calls
            return ResponseEntity.ok(List.of());
        }
        List<LocationSuggestionDTO> suggestions = locationService.getSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
}