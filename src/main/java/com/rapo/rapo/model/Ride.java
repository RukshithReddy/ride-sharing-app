package com.rapo.rapo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Change to EAGER to always load driver info
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @ManyToOne(fetch = FetchType.EAGER) // Change to EAGER to always load vehicle info
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private int availableSeats;
    
    // --- Distance And Ridestatus ---

    @Column
    private Double distanceInKm;

    @Column
    private Double totalFare;

    @Enumerated(EnumType.STRING)
    @Column
    private RideStatus rideStatus;
    
    @OneToMany(mappedBy = "ride")
    @JsonIgnore // Ignore bookings to prevent loops
    private List<Booking> bookings;
}