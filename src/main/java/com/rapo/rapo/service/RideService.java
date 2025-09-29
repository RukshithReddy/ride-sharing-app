package com.rapo.rapo.service;

import com.rapo.rapo.dto.BookingRequest;
import com.rapo.rapo.dto.BookingResponse;
import com.rapo.rapo.dto.RideRequest;
import com.rapo.rapo.model.Booking;
import com.rapo.rapo.model.Ride;
import com.rapo.rapo.model.User;
import com.rapo.rapo.repository.BookingRepository;
import com.rapo.rapo.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FareService fareService;
//    @Autowired
//    private GeocodingService geocodingService;

    public Ride postRide(RideRequest rideRequest, User driver) {
//    	 double[] sourceCoords = geocodingService.getCoordinates(rideRequest.getSource());
//       double[] destCoords = geocodingService.getCoordinates(rideRequest.getDestination());

        Ride ride = new Ride();
        
        ride.setDriver(driver);
        // Assuming the driver has one vehicle. If not, logic to select one would be needed.
        ride.setVehicle(driver.getVehicle());
        ride.setSource(rideRequest.getSource());
        ride.setDestination(rideRequest.getDestination());
        ride.setDepartureTime(rideRequest.getDateTime());
        ride.setAvailableSeats(rideRequest.getAvailableSeats());
        
        
        // Calculate distance and fare using coordinates
        double distance = fareService.getDistance(
            rideRequest.getStartLatitude(),
            rideRequest.getStartLongitude(),
            rideRequest.getEndLatitude(),
            rideRequest.getEndLongitude()
        );
        double fare = fareService.calculateFare(distance);

        ride.setDistanceInKm(distance);
        ride.setTotalFare(fare);
        
        return rideRepository.save(ride);
    }

    public List<Ride> searchRides(String source, String destination, LocalDate date) {
        return rideRepository.findAvailableRides(source, destination, date.atStartOfDay());
        
    }

    @Transactional
    public Booking bookRide(BookingRequest bookingRequest, User passenger) {
        Ride ride = rideRepository.findById(bookingRequest.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats for this ride.");
        }

        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);

        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        return bookingRepository.save(booking);
    }
    
    public List<Ride> getDriverRides(Long driverId){
        return rideRepository.findByDriverId(driverId);
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponse> getPassengerBookings(Long passengerId){
        List<Booking> bookings = bookingRepository.findByPassengerId(passengerId);
        
        return bookings.stream().map(booking -> {
            BookingResponse response = new BookingResponse();
            response.setBookingId(booking.getId());
            
            Ride ride = booking.getRide();
            response.setSource(ride.getSource());
            response.setDestination(ride.getDestination());
            response.setDepartureTime(ride.getDepartureTime());
            response.setDriverName(ride.getDriver().getName());
            
            return response;
        }).collect(Collectors.toList());
    }  
 
}
