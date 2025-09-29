package com.rapo.rapo.controller;

import com.rapo.rapo.dto.BookingRequest;
import com.rapo.rapo.dto.BookingResponse;
import com.rapo.rapo.dto.RideRequest;
import com.rapo.rapo.model.Booking;
import com.rapo.rapo.model.Ride;
import com.rapo.rapo.model.User;
import com.rapo.rapo.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.rapo.rapo.dto.FareEstimateRequestDTO;
import com.rapo.rapo.dto.FareEstimateResponseDTO;
import com.rapo.rapo.service.FareService;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/rides")
    public ResponseEntity<Ride> postRide(@Valid @RequestBody RideRequest rideRequest, @AuthenticationPrincipal User driver) {
        System.out.println(">>> [DEBUG] postRide called by user: " + driver.getEmail() + " with roles: " + driver.getAuthorities());

    	Ride ride = rideService.postRide(rideRequest, driver);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/rides/search")
    public ResponseEntity<List<Ride>> searchRides(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Ride> rides = rideService.searchRides(source, destination, date);
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> bookRide(@Valid @RequestBody BookingRequest bookingRequest, @AuthenticationPrincipal User passenger) {
        try {
            Booking booking = rideService.bookRide(bookingRequest, passenger);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/rides/my-rides")
    public ResponseEntity<List<Ride>> getMyPostedRides(@AuthenticationPrincipal User driver) {
        return ResponseEntity.ok(rideService.getDriverRides(driver.getId()));
    }

    @GetMapping("/users/me/bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal User passenger) {
        return ResponseEntity.ok(rideService.getPassengerBookings(passenger.getId()));
    }
    @Autowired
    private FareService fareService;

    @PostMapping("/fare-estimate")
    public ResponseEntity<FareEstimateResponseDTO> getFareEstimate(@RequestBody FareEstimateRequestDTO request) {
        try {
            double distance = fareService.getDistance(
                request.getStartLatitude(),
                request.getStartLongitude(),
                request.getEndLatitude(),
                request.getEndLongitude()
            );
            double fare = fareService.calculateFare(distance);
            
            FareEstimateResponseDTO response = new FareEstimateResponseDTO(distance, fare);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
