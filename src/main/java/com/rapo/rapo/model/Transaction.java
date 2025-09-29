package com.rapo.rapo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "passenger_id", referencedColumnName = "id", nullable = false)
    private User passenger;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;
    
    // This will store masked data, e.g., "************1234"
    @Column
    private String paymentDetails;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionTimestamp = LocalDateTime.now();
}