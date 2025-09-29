package com.rapo.rapo.repository;

import com.rapo.rapo.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    // --- UPDATED METHOD SIGNATURE ---
    // Added @Param("date") to correctly map the method parameter to the query parameter.
    @Query("SELECT r FROM Ride r WHERE r.source LIKE %:source% AND r.destination LIKE %:destination% AND r.departureTime >= :date AND r.availableSeats > 0")
    List<Ride> findAvailableRides(@Param("source") String source, @Param("destination") String destination, @Param("date") LocalDateTime date);

    List<Ride> findByDriverId(Long driverId);
}